package com.mediamarktsaturn.productdomain.converters;

import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.payload.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryResponse fromCategory(Category category) {
        Category superCategory = category.getSuperCategory();
        return new CategoryResponse(category.getCategoryId(), category.getName(),
                superCategory != null ? superCategory.getCategoryId() : null);
    }

}
