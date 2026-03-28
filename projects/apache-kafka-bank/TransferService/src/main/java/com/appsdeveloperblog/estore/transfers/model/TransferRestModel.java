package com.appsdeveloperblog.estore.transfers.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class TransferRestModel {
    private String senderId;
    private String recipientId;
    private BigDecimal amount;
}
