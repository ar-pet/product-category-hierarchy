package com.mediamarktsaturn.productdomain.exception;

public class CategoryInvalidException extends RuntimeException{
    public CategoryInvalidException(long id) {
        super("Category can not be updated for the id " + id);
    }

    public CategoryInvalidException() {
        super("Category can not be created");
    }
}
