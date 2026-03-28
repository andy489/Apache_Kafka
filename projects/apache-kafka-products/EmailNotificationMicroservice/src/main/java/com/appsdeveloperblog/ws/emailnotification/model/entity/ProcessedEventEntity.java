package com.appsdeveloperblog.ws.emailnotification.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="processed-events")
@Getter
@Setter
@Accessors(chain=true)
public class ProcessedEventEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3687553269742697084L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    private String messageId;

    @Column(unique=true)
    private String productId;
}
