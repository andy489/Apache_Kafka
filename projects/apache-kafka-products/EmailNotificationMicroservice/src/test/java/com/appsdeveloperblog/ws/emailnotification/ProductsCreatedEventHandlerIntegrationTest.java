package com.appsdeveloperblog.ws.emailnotification;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.emailnotification.handler.ProductCreatedEventHandler;
import com.appsdeveloperblog.ws.emailnotification.model.entity.ProcessedEventEntity;
import com.appsdeveloperblog.ws.emailnotification.repository.ProcessedEventRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@EmbeddedKafka(
        partitions = 3,
        topics = {"product-created-events-topic"}
)
@SpringBootTest(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest",
})
public class ProductsCreatedEventHandlerIntegrationTest {

    @MockitoBean
    private ProcessedEventRepository mockProcessedEventRepository;

    @MockitoBean
    private RestTemplate mockRestTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoSpyBean
    private ProductCreatedEventHandler spyMockProductCreatedEventHandler;

    @Test
    void testProductCreatedEventHandler_OnProductCreated_HandlesEvent() throws ExecutionException, InterruptedException, TimeoutException {

        // Arrange
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent()
                .setPrice(new BigDecimal("10.99"))
                .setProductId(UUID.randomUUID().toString())
                .setQuantity(1)
                .setTitle("Test product");

        String messageId = UUID.randomUUID().toString();
        String messageKey = productCreatedEvent.getProductId();

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "product-created-events-topic",
                messageKey,
                productCreatedEvent
        );

        record.headers().add("messageId", messageId.getBytes());
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.getBytes());

        // Mock repository - return empty so handler processes the event
        when(mockProcessedEventRepository.findByMessageId(anyString()))
                .thenReturn(Optional.empty());

        when(mockProcessedEventRepository.save(any(ProcessedEventEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock REST call
        String responseBody = "{\"key\":\"value\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);

        when(mockRestTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                isNull(),
                eq(String.class))).thenReturn(responseEntity);

        // Act - Send real Kafka message
        kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);

        // Wait for async processing
        Thread.sleep(3000);

        // Assert
        ArgumentCaptor<String> messageIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageKeyArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProductCreatedEvent> productCreatedEventArgumentCaptor =
                ArgumentCaptor.forClass(ProductCreatedEvent.class);

        // Verify the handler was called
        verify(spyMockProductCreatedEventHandler, timeout(10000).times(1))
                .handle(productCreatedEventArgumentCaptor.capture(),
                        messageIdArgumentCaptor.capture(),
                        messageKeyArgumentCaptor.capture());

        // Verify captured values
        assertEquals(messageId, messageIdArgumentCaptor.getValue(),
                "Message ID mismatch - Expected: " + messageId + " but was: " + messageIdArgumentCaptor.getValue());

        assertEquals(messageKey, messageKeyArgumentCaptor.getValue(),
                "Message key mismatch - Expected: " + messageKey + " but was: " + messageKeyArgumentCaptor.getValue());

        assertEquals(productCreatedEvent.getTitle(),
                productCreatedEventArgumentCaptor.getValue().getTitle(),
                "Product title mismatch - Expected: " + productCreatedEvent.getTitle() +
                        " but was: " + productCreatedEventArgumentCaptor.getValue().getTitle());

        assertEquals(productCreatedEvent.getPrice(),
                productCreatedEventArgumentCaptor.getValue().getPrice(),
                "Product price mismatch - Expected: " + productCreatedEvent.getPrice() +
                        " but was: " + productCreatedEventArgumentCaptor.getValue().getPrice());

        assertEquals(productCreatedEvent.getQuantity(),
                productCreatedEventArgumentCaptor.getValue().getQuantity(),
                "Product quantity mismatch - Expected: " + productCreatedEvent.getQuantity() +
                        " but was: " + productCreatedEventArgumentCaptor.getValue().getQuantity());

        // Verify repository was called
        verify(mockProcessedEventRepository, timeout(10000).times(1))
                .findByMessageId(messageId);
        verify(mockProcessedEventRepository, timeout(10000).times(1))
                .save(any(ProcessedEventEntity.class));

        // Verify REST call
        verify(mockRestTemplate, timeout(10000).times(1))
                .exchange(anyString(), any(HttpMethod.class), isNull(), eq(String.class));
    }
}