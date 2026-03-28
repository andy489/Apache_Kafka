package com.appsdeveloperblog.ws.products.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;

@Service
public class ProductServiceImpl implements ProductService {

    private final boolean reactiveEnabled = false;
    private static final long KAFKA_SEND_TIMEOUT_SECONDS = 10;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {

        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into database table before publishing an Event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(), productRestModel.getPrice(),
                productRestModel.getQuantity());

        LOGGER.info("Before publishing a ProductCreatedEvent");

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                productCreatedEvent);

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        if (reactiveEnabled) {
            sendMessageAsync(record);
        } else {
            sendMessageSync(record);
        }

        return productId;
    }

    private void sendMessageAsync(ProducerRecord<String, ProductCreatedEvent> record) {
        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send(record);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage(), exception);
                // You might want to store failed messages in a database for retry
            } else {
                logSendResult(result);
            }
        });
    }

    private void sendMessageSync(ProducerRecord<String, ProductCreatedEvent> record) throws Exception {
        try {
            SendResult<String, ProductCreatedEvent> result =
                    kafkaTemplate.send(record).get(KAFKA_SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            logSendResult(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Message sending was interrupted", e);
            throw new Exception("Failed to send message: Operation was interrupted", e);
        } catch (ExecutionException e) {
            LOGGER.error("Failed to send message", e);
            throw new Exception("Failed to send message: " + e.getCause().getMessage(), e);
        } catch (TimeoutException e) {
            LOGGER.error("Message sending timed out after {} seconds", KAFKA_SEND_TIMEOUT_SECONDS, e);
            throw new Exception("Failed to send message: Timeout waiting for Kafka response", e);
        }
    }

    private void logSendResult(SendResult<String, ProductCreatedEvent> result) {
        LOGGER.info("Successfully sent message");
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("***** Returning product id");
    }
}