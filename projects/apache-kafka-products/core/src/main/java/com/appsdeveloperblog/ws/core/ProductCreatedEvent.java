package com.appsdeveloperblog.ws.core;

import java.math.BigDecimal;

public class ProductCreatedEvent {

    private String productId;

    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String productId, String title, BigDecimal price, Integer quantity) {
        this.price = price;
        this.productId = productId;
        this.quantity = quantity;
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductCreatedEvent setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public ProductCreatedEvent setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductCreatedEvent setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductCreatedEvent setTitle(String title) {
        this.title = title;
        return this;
    }
}
