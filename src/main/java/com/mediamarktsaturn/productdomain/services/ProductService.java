package com.mediamarktsaturn.productdomain.services;

import com.mediamarktsaturn.productdomain.converters.ProductConverter;
import com.mediamarktsaturn.productdomain.exception.ProductInvalidException;
import com.mediamarktsaturn.productdomain.exception.ProductNotFoundException;
import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.OnlineStatus;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.payload.ProductRequest;
import com.mediamarktsaturn.productdomain.payload.ProductResponse;
import com.mediamarktsaturn.productdomain.repositories.CategoryRepository;
import com.mediamarktsaturn.productdomain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductConverter productConverter;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productConverter = productConverter;
    }

    @Transactional
    public void deleteProduct(Long id) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Set<Category> productCategories = product.getCategories();
        productCategories.forEach(category -> category.getProducts().remove(product));

        product.setStatus(OnlineStatus.DELETED);
        productRepository.save(product);

        categoryRepository.saveAll(productCategories);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        if(productRequest.getName() == null) {
            throw new ProductInvalidException();
        }

        Set<Long> categoryIds = productRequest.getCategoryIds();
        if(categoryIds.stream().anyMatch(categoryId -> !categoryRepository.existsById(categoryId))) {
            throw new ProductInvalidException();
        }
        var product = productConverter.fromProductRequest(productRequest);

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).get();
            product.addCategory(category);
        }

        Product newProduct = productRepository.save(product);

        return productConverter.fromProduct(newProduct);
    }

    public Set<String> getCategoryPaths(long productId) {
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Set<String> categoryPaths = new HashSet<>();
        Set<Category> productCategories = product.getCategories();

        for (Category category : productCategories) {
            StringBuilder pathBuilder = new StringBuilder();
            generateCategoryPathToRoot(category, pathBuilder);
            categoryPaths.add(pathBuilder.toString());
        }

        return categoryPaths;
    }

    private void generateCategoryPathToRoot(Category category, StringBuilder pathBuilder) {
        pathBuilder.append(category.getCategoryId()).append("->");
        if(category.getSuperCategory() != null) {
            generateCategoryPathToRoot(category.getSuperCategory(), pathBuilder);
        }
    }

    public ProductResponse getProductResponseById(long id) {
        return productConverter.fromProduct(getProductById(id));
    }

    private Product getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public ProductResponse updateProduct(long id, ProductRequest productRequest) {
        if(productRequest.getName() == null) {
            throw new ProductInvalidException();
        }
        var sourceProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Set<Long> categoryIds = productRequest.getCategoryIds();
        if(categoryIds.stream().anyMatch(categoryId -> !categoryRepository.existsById(categoryId))) {
            throw new ProductInvalidException();
        }

        sourceProduct.setName(productRequest.getName());
        sourceProduct.setShortDescription(productRequest.getShortDescription());
        sourceProduct.setLongDescription(productRequest.getLongDescription());
        sourceProduct.setStatus(productRequest.getStatus());

        sourceProduct.removeAllCategories();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).get();
            sourceProduct.addCategory(category);
        }

        sourceProduct = productRepository.save(sourceProduct);

        return productConverter.fromProduct(sourceProduct);
    }
}
