package com.appsdeveloperblog.ws.emailnotification.repository;

import com.appsdeveloperblog.ws.emailnotification.model.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, Long> {

    Optional<ProcessedEventEntity> findByMessageId(String messageId);
}
