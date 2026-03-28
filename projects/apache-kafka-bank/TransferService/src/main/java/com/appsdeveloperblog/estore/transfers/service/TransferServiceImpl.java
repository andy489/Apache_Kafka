package com.appsdeveloperblog.estore.transfers.service;

import com.appsdeveloperblog.estore.transfers.io.TransferEntity;
import com.appsdeveloperblog.estore.transfers.io.TransferRepository;
import org.apache.kafka.common.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.estore.transfers.error.TransferServiceException;
import com.appsdeveloperblog.estore.transfers.model.TransferRestModel;
import com.appsdeveloperblog.payments.ws.core.events.DepositRequestedEvent;
import com.appsdeveloperblog.payments.ws.core.events.WithdrawalRequestedEvent;

@Service
public class TransferServiceImpl implements TransferService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Environment environment;
    private final RestTemplate restTemplate;
    private final TransferRepository transferRepository;

    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment,
                               RestTemplate restTemplate, TransferRepository transferRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
        this.restTemplate = restTemplate;
        this.transferRepository = transferRepository;
    }

    @Transactional(value = "transactionManager" /*value = "kafkaTransactionManager" ,
            rollbackFor = {TransferServiceException.class, ConnectException.class}, (ot Throwable.class)
            noRollbackFor = {SpecificException.class}*/)
    @Override
    public boolean transfer(TransferRestModel transferRestModel) {
        WithdrawalRequestedEvent withdrawalEvent = new WithdrawalRequestedEvent()
                .setSenderId(transferRestModel.getSenderId())
                .setRecipientId(transferRestModel.getRecipientId())
                .setAmount(transferRestModel.getAmount());

        DepositRequestedEvent depositEvent = new DepositRequestedEvent()
                .setSenderId(transferRestModel.getSenderId())
                .setRecipientId(transferRestModel.getRecipientId())
                .setAmount(transferRestModel.getAmount());

        // Prepare object to save into a db
        TransferEntity transferEntity = new TransferEntity()
                .setTransferId(Uuid.randomUuid().toString());
        BeanUtils.copyProperties(transferRestModel, transferEntity);

        try {
            // Save record to a database table
            transferRepository.save(transferEntity);

            kafkaTemplate.send(environment.getProperty("withdraw-money-topic", "withdraw-money-topic"),
                    withdrawalEvent);
            LOGGER.info("Sent event to withdrawal topic.");

            // Business logic that might cause an error
            ResponseEntity<String> stringResponseEntity = callRemoteService();

            kafkaTemplate.send(environment.getProperty("deposit-money-topic", "deposit-money-topic"), depositEvent);
            LOGGER.info("Sent event to deposit topic");

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new TransferServiceException(ex);
        }

        return true;
    }

    private ResponseEntity<String> callRemoteService() throws Exception {
        String requestUrl = "http://localhost:8082/response/200";
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

        if (response.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw new Exception("Destination Microservice not available");
        }

        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            LOGGER.info("Received response from mock service: {}", response.getBody());
        }

        return response;
    }
}
