package com.appsdeveloperblog.ws.emailnotification.handler;

import com.appsdeveloperblog.ws.emailnotification.error.NotRetryableException;
import com.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import com.appsdeveloperblog.ws.emailnotification.model.entity.ProcessedEventEntity;
import com.appsdeveloperblog.ws.emailnotification.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@KafkaListener(topics = "product-created-events-topic"/*, groupId="product-created-events"*/)
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private final ProcessedEventRepository processedEventRepository;

    public ProductCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepository processedEventRepository) {
        this.restTemplate = restTemplate;
        this.processedEventRepository = processedEventRepository;
    }

    @KafkaHandler
    @Transactional
    public void handle(@Payload ProductCreatedEvent productCreatedEvent,
                       @Header(value = "messageId") String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {

        LOGGER.info("Received a new event: {} with productId: {}", productCreatedEvent.getTitle(),
                productCreatedEvent.getProductId());

        // Check if this message was already processed before
        Optional<ProcessedEventEntity> byMessageId = processedEventRepository.findByMessageId(messageId);

        if (byMessageId.isPresent()) {
            LOGGER.info("Found a duplicate message id: {}", byMessageId.get().getMessageId());
            return;
        }

        String requestUrl = "http://localhost:8082/response/200";
//        String requestUrl = "http://localhost:8082/response/500";

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET,
                    null,
                    String.class);

            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                LOGGER.info("Received response from a remote service: {}", response.getBody());
            }

        } catch (ResourceAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw new RetryableException(ex);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

        // Save a unique message id in a database table
        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity()
                .setProductId(productCreatedEvent.getProductId())
                .setMessageId(messageId);

        try {
            processedEventRepository.save(processedEventEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new NotRetryableException(ex);
        }
    }
}
