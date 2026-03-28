package com.appsdeveloperblog.ws.products;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import com.appsdeveloperblog.ws.products.service.ProductService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext // ensures that each test will start with a clean state
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // application-test.yml
@EmbeddedKafka(
        partitions = 3,
        count = 3,
        controlledShutdown = true,
        topics = {"product-created-events-topic"}
)
@SpringBootTest(
        properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
public class ProductsServiceIntegrationTest {

    private static final String TOPIC_NAME = "product-created-events-topic";

    private ProductService productService;

    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Environment environment;

    private KafkaMessageListenerContainer<String, ProductCreatedEvent> container;

    private BlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> records;

    // execute one time only before all test methods in the class
    @BeforeAll
    void setUp() {
        // Create consumer factory
        DefaultKafkaConsumerFactory<String, ProductCreatedEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(getConsumerProperties());

        // Create container properties with the topic
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);

        // Create and configure container
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();

        container.setupMessageListener((MessageListener<String, ProductCreatedEvent>) records::add);
        container.start();

        // Wait for container to be assigned to partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        if (container != null) {
            container.stop();
        }
    }

    @Autowired
    public ProductsServiceIntegrationTest(ProductService productService,
                                          EmbeddedKafkaBroker embeddedKafkaBroker,
                                          Environment environment) {
        this.productService = productService;
        this.embeddedKafkaBroker = embeddedKafkaBroker;
        this.environment = environment;
    }

    @Test
    void testCreateProduct_whenGivenValidProductDetails_successfulSendsKafkaMessage() throws Exception {

        // Arrange
        String title = "iPhone 15";
        BigDecimal price = new BigDecimal(790);
        Integer quantity = 3;

        CreateProductRestModel createProductRestModel = new CreateProductRestModel()
                .setTitle(title)
                .setPrice(price)
                .setQuantity(quantity);

        // Act
        String productId = productService.createProduct(createProductRestModel);

        // Verify product ID was returned
        assertNotNull(productId, "Product ID should not be null");

        // Assert
        ConsumerRecord<String, ProductCreatedEvent> message = records.poll(3000, TimeUnit.MILLISECONDS);

        // Verify message was received from Kafka
        assertNotNull(message, "Expected a Kafka message but none was received within 5 seconds");

        // Verify message has a valid key (product ID)
        assertNotNull(message.key(), "Message key (productId) should not be null");

        ProductCreatedEvent productCreatedEvent = message.value();

        // Verify all product details were correctly sent in the event
        assertEquals(createProductRestModel.getTitle(), productCreatedEvent.getTitle(),
                "Product title mismatch - expected: " + createProductRestModel.getTitle() +
                        " but was: " + productCreatedEvent.getTitle());

        assertEquals(createProductRestModel.getPrice(), productCreatedEvent.getPrice(),
                "Product price mismatch - expected: " + createProductRestModel.getPrice() +
                        " but was: " + productCreatedEvent.getPrice());

        assertEquals(createProductRestModel.getQuantity(), productCreatedEvent.getQuantity(),
                "Product quantity mismatch - expected: " + createProductRestModel.getQuantity() +
                        " but was: " + productCreatedEvent.getQuantity());
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.group-id"),
                JsonDeserializer.TRUSTED_PACKAGES,
                environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                environment.getProperty("spring.kafka.consumer.auto-offset-reset")
        );
    }
}
