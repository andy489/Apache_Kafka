package com.appsdeveloperblog.payments.dao.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "payments")
@Entity
@Getter
@Setter
@Accessors(chain = true)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "product_price")
    private BigDecimal productPrice;
    @Column(name = "product_quantity")
    private Integer productQuantity;
}
