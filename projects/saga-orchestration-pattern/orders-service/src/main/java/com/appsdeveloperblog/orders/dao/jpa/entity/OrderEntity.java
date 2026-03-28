package com.appsdeveloperblog.orders.dao.jpa.entity;

import com.appsdeveloperblog.core.types.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Table(name = "orders")
@Entity
@Getter
@Setter
@Accessors(chain = true)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "product_quantity")
    private Integer productQuantity;
}
