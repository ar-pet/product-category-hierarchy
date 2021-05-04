package com.mediamarktsaturn.productdomain.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(long id) {
        super("Category not found for id " + id);
    }
}
