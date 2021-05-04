package com.mediamarktsaturn.productdomain.repositories;

import com.mediamarktsaturn.productdomain.models.Category;

public interface CategoryRepositoryCustom {
    Category getCategoryByIdAndFetchProducts(Long categoryId);
}
