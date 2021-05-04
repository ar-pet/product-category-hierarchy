package com.mediamarktsaturn.productdomain.payload;

public final class CategoryResponse {
    private final Long id;
    private final String name;
    private final Long superCategoryId;

    public CategoryResponse(Long id, String name, Long superCategoryId) {
        this.id = id;
        this.name = name;
        this.superCategoryId = superCategoryId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSuperCategoryId() {
        return superCategoryId;
    }
}
