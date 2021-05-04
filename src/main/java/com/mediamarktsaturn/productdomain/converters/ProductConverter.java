package com.mediamarktsaturn.productdomain.converters;

import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.payload.ProductRequest;
import com.mediamarktsaturn.productdomain.payload.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    public ProductResponse fromProduct(Product product) {
        Set<Category> productCategories = product.getCategories();
        Set<Long> productCategoryIds = productCategories
                .stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toSet());

        return new ProductResponse(product.getProductId(), product.getName(),
                product.getShortDescription(), product.getLongDescription(), product.getStatus(), productCategoryIds);
    }

    public Product fromProductRequest(ProductRequest productRequest) {
        var product = new Product();
        product.setName(productRequest.getName());
        product.setStatus(productRequest.getStatus());
        product.setShortDescription(productRequest.getShortDescription());
        product.setLongDescription(productRequest.getLongDescription());

        return product;
    }
}
