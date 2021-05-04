package com.mediamarktsaturn.productdomain.exception;

public class ProductInvalidException extends RuntimeException {
    public ProductInvalidException(long id) {
        super("Product can not be updated for the id " + id);
    }

    public ProductInvalidException() {
        super("Product can not be created because of the invalid data");
    }
}
