package com.mediamarktsaturn.productdomain.services;

import com.mediamarktsaturn.productdomain.converters.ProductConverter;
import com.mediamarktsaturn.productdomain.exception.ProductInvalidException;
import com.mediamarktsaturn.productdomain.exception.ProductNotFoundException;
import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.OnlineStatus;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.payload.ProductRequest;
import com.mediamarktsaturn.productdomain.repositories.CategoryRepository;
import com.mediamarktsaturn.productdomain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductConverter productConverter;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private ProductService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new ProductService(productRepository, categoryRepository, productConverter);
    }

    @Test
    void updateProduct_wrong_productId() {
        final long invalidProductId = 1;
        when(productRepository.findById(anyLong())).thenThrow(new ProductNotFoundException(invalidProductId));

        assertThrows(ProductNotFoundException.class, () ->
                underTest.updateProduct(invalidProductId,
                        new ProductRequest("productName", null, null, null, null)));
    }

    @Test
    void updateProduct_wrong_categoryId() {
        final long productId = 1;
        final long invalidCategoryId = 2;

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(categoryRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProductInvalidException.class, () ->
                underTest.updateProduct(productId,
                        new ProductRequest("productName", null, null, null, Set.of(invalidCategoryId))));
    }

    @Test
    void updateProduct_success() {
        final String productUpdatedName = "updateName";
        final String productName = "name";

        final long productUpdatedCategoryId = 2;
        final long categoryId = 1;
        final long productId = 1;

        ProductRequest updateRequest = new ProductRequest(productUpdatedName, null, null, OnlineStatus.ACTIVE,
                Set.of(productUpdatedCategoryId));

        Category sourceProductCategory = createCategory(categoryId, "categoryName");
        Category updatedCategory = createCategory(productUpdatedCategoryId, "updatedCategoryName");

        Product sourceProduct = createActiveProduct(productName,
                new HashSet<>(Collections.singletonList((sourceProductCategory))));

        when(productRepository.findById(productId)).thenReturn(Optional.of(sourceProduct));
        when(categoryRepository.existsById(productUpdatedCategoryId)).thenReturn(true);
        when(categoryRepository.findById(productUpdatedCategoryId)).thenReturn(Optional.of(updatedCategory));

        underTest.updateProduct(productId, updateRequest);

        verify(productRepository).save(productCaptor.capture());
        Product updateProduct = productCaptor.getValue();
        assertThat(updateProduct.getName()).isEqualTo(productUpdatedName);
        assertThat(updateProduct.getStatus()).isEqualTo(OnlineStatus.ACTIVE);

        Set<Category> productCategories = updateProduct.getCategories();
        assertThat(productCategories.size()).isEqualTo(1);
        assertThat(productCategories.stream().findFirst().get().getCategoryId()).isEqualTo(productUpdatedCategoryId);
    }

    private Category createCategory(long categoryId, String name) {
        Category category = new Category();
        category.setName(name);
        category.setCategoryId(categoryId);

        return category;
    }

    private Product createActiveProduct(String name, Set<Category> categories) {
        Product sourceProduct = new Product();
        sourceProduct.setName(name);
        sourceProduct.setStatus(OnlineStatus.ACTIVE);
        sourceProduct.setCategories(categories);

        return sourceProduct;
    }
}
