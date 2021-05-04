package com.mediamarktsaturn.productdomain.payload;

public final class CategoryRequest {
    private final String name;
    private final Long superCategoryId;

    public CategoryRequest(String name, Long superCategoryId) {
        this.name = name;
        this.superCategoryId = superCategoryId;
    }

    public String getName() {
        return name;
    }

    public Long getSuperCategoryId() {
        return superCategoryId;
    }
}
