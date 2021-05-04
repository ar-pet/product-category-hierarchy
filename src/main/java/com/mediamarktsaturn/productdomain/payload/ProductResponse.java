package com.mediamarktsaturn.productdomain.payload;

import com.mediamarktsaturn.productdomain.models.OnlineStatus;

import java.util.HashSet;
import java.util.Set;

public final class ProductResponse {

    public ProductResponse(Long id, String name, String shortDescription, String longDescription, OnlineStatus status,
                           Set<Long> categoryIds) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.status = status;
        if (categoryIds == null) {
            this.categoryIds = new HashSet<>();
        } else {
            this.categoryIds = categoryIds;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    private final Long id;
    private final String name;
    private final String shortDescription;
    private final String longDescription;
    private final OnlineStatus status;
    private final Set<Long> categoryIds;
}
