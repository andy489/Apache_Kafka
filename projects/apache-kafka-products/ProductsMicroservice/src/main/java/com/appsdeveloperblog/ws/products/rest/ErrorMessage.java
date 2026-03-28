package com.appsdeveloperblog.ws.products.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorMessage {

    private Date timestamp;
    private String message;
    private String details;
}
