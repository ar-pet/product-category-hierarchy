package com.mediamarktsaturn.productdomain.exception;

public class CategoryDeleteException extends RuntimeException {

    public CategoryDeleteException(long id) {
        super("Category can not be deleted for id " + id);
    }
}
