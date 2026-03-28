package com.appsdeveloperblog.ws.products.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.ws.products.service.ProductService;

@RestController
@RequestMapping("/products") //http://localhost:<port>/products
public class ProductController {

    private final ProductService productService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRestModel product) {

        String productId;
        try {
            productId = productService.createProduct(product);
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error(e.getMessage(), e);

            ErrorMessage errorMessage = new ErrorMessage()
                    .setTimestamp(new Date())
                    .setMessage(e.getMessage())
                    .setDetails("/products");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

}
