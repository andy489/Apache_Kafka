package com.appsdeveloperblog.orders.dto;

import com.appsdeveloperblog.core.types.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class OrderHistoryResponse {
    private UUID id;
    private UUID orderId;
    private OrderStatus status;
    private Timestamp createdAt;
}
