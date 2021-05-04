package com.mediamarktsaturn.productdomain.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(long id) {
        super("Product not found for id " + id);
    }
}
