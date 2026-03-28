package com.appsdeveloperblog.ws.products;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@SpringBootTest
public class IdempotentProducerIntegrationTest {

    @Autowired
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @MockitoBean
    KafkaAdmin kafkaAdmin;

    @Test
    void testProducerConfig_whenIdempotenceEnabled_assertsIdempotentProperties() {
        // Arrange
        ProducerFactory<String, ProductCreatedEvent> producerFactory = kafkaTemplate.getProducerFactory();

        // Act
        Map<String, Object> config = producerFactory.getConfigurationProperties();

        // Log configuration for debugging
        System.out.println("Producer Configuration: " + config);

        // Assert 1: Verify idempotence is enabled
        Object idempotenceValue = config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG);
        Assertions.assertNotNull(
                idempotenceValue,
                "ENABLE_IDEMPOTENCE_CONFIG should be present in producer configuration"
        );

        Assertions.assertTrue(
                (Boolean) idempotenceValue,
                String.format(
                        "Idempotence must be enabled for exactly-once delivery semantics.%n" +
                                "Expected: %s = true%n" +
                                "Actual: %s = %s%n" +
                                "Please check spring.kafka.producer.properties.enable.idempotence in application.yml",
                        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                        idempotenceValue
                )
        );

        // Assert 2: Verify acks configuration
        Object acksValue = config.get(ProducerConfig.ACKS_CONFIG);
        Assertions.assertNotNull(
                acksValue,
                "ACKS_CONFIG should be present in producer configuration"
        );

        Assertions.assertEquals(
                "all",
                String.valueOf(acksValue).toLowerCase(),
                String.format(
                        "When idempotence is enabled, acks must be set to 'all' to guarantee data durability.%n" +
                                "Expected: %s = 'all'%n" +
                                "Actual: %s = %s%n" +
                                "Please check spring.kafka.producer.acks in application.yml",
                        ProducerConfig.ACKS_CONFIG,
                        ProducerConfig.ACKS_CONFIG,
                        acksValue
                )
        );

        // Assert 3: Verify max.in.flight.requests.per.connection is <= 5 for idempotence
        Object maxInFlightValue = config.get(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION);
        if (maxInFlightValue != null) {
            int maxInFlight = Integer.parseInt(maxInFlightValue.toString());
            Assertions.assertTrue(
                    maxInFlight <= 5,
                    String.format(
                            "When idempotence is enabled, max.in.flight.requests.per.connection must be <= 5%n" +
                                    "to guarantee ordering. Current value: %d%n" +
                                    "Please check spring.kafka.producer.properties.max.in.flight.requests.per.connection",
                            maxInFlight
                    )
            );
        }

        // Assert 4: Verify retries are configured appropriately
        // Note: When idempotence is enabled, retries default to Integer.MAX_VALUE even if not explicitly set
        Object retriesValue = config.get(ProducerConfig.RETRIES_CONFIG);

        if (retriesValue != null) {
            int retries = Integer.parseInt(retriesValue.toString());
            Assertions.assertTrue(
                    retries > 0,
                    String.format(
                            "When idempotence is enabled, retries should be > 0.%n" +
                                    "Current value: %d%n" +
                                    "Please check spring.kafka.producer.retries in application.yml",
                            retries
                    )
            );
        } else {
            // When retries are not explicitly configured, Spring/Kafka defaults to Integer.MAX_VALUE
            // when idempotence is enabled. This is acceptable.
            System.out.println("RETRIES_CONFIG not explicitly set - using default (Integer.MAX_VALUE) which is correct " +
                    "for idempotent producer");
        }

        // Assert 5: Verify delivery timeout is configured appropriately
        Object deliveryTimeoutValue = config.get(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG);
        if (deliveryTimeoutValue != null) {
            int deliveryTimeout = Integer.parseInt(deliveryTimeoutValue.toString());
            Assertions.assertTrue(
                    deliveryTimeout >= 120000,
                    String.format(
                            "Delivery timeout should be at least 120000 ms (2 minutes) for reliable delivery.%n" +
                                    "Current value: %d ms%n" +
                                    "Please check spring.kafka.producer.properties.delivery.timeout.ms",
                            deliveryTimeout
                    )
            );
        }

        // Assert 6: Verify request timeout
        Object requestTimeoutValue = config.get(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG);
        if (requestTimeoutValue != null) {
            int requestTimeout = Integer.parseInt(requestTimeoutValue.toString());
            Assertions.assertTrue(
                    requestTimeout >= 30000,
                    String.format(
                            "Request timeout should be at least 30000 ms (30 seconds).%n" +
                                    "Current value: %d ms",
                            requestTimeout
                    )
            );
        }

        System.out.println("Idempotent producer configuration verified successfully!");
    }
}