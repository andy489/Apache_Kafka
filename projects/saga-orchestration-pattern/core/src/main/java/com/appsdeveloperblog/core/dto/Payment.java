package com.appsdeveloperblog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private BigDecimal productPrice;
    private Integer productQuantity;

    public Payment(UUID orderId, UUID productId, BigDecimal productPrice, Integer productQuantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }
}

