package com.appsdeveloperblog.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationFailedEvent {
    private UUID productId;
    private UUID orderId;
    private Integer productQuantity;
}
