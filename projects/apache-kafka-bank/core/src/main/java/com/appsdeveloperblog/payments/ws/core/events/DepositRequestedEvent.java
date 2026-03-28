package com.appsdeveloperblog.payments.ws.core.events;

import java.math.BigDecimal;

public class DepositRequestedEvent {

    private String senderId;
    private String recipientId;
    private BigDecimal amount;

    public DepositRequestedEvent() {
    }

    public DepositRequestedEvent(BigDecimal amount, String recipientId, String senderId) {
        this.amount = amount;
        this.recipientId = recipientId;
        this.senderId = senderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DepositRequestedEvent setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public DepositRequestedEvent setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public DepositRequestedEvent setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }
}