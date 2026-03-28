package com.appsdeveloperblog.ws.products.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class CreateProductRestModel {

    private String title;
    private BigDecimal price;
    private Integer quantity;
}
