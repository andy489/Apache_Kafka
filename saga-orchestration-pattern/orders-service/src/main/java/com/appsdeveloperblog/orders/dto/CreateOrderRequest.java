package com.appsdeveloperblog.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID productId;
    @NotNull
    @Positive
    private Integer productQuantity;
}
