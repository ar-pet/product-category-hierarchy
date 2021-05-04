package com.mediamarktsaturn.productdomain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.OnlineStatus;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.repositories.CategoryRepository;
import com.mediamarktsaturn.productdomain.repositories.ProductRepository;
import com.mediamarktsaturn.productdomain.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void cleanDatabase() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void deleteProduct_success() throws Exception {
        Category category = categoryRepository.save(new Category("someCategoryName"));
        Product product = productRepository.save(new Product("product1Name", OnlineStatus.ACTIVE,
                Set.of(category)));

        category.addProduct(product);
        categoryRepository.save(category);

        Product productToDelete = productRepository.save(new Product("product2Name", OnlineStatus.ACTIVE,
                Set.of(category)));
        final Long productToDeleteId = productToDelete.getProductId();

        mockMvc.perform(
                delete("/productdomain/api/product" + "/" + productToDelete.getProductId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        Optional<Product> productToDeleteCheckedOpt = productRepository.findById(productToDelete.getProductId());
        assertThat(productToDeleteCheckedOpt).isPresent();
        assertThat(productToDeleteCheckedOpt.get().getStatus()).isEqualTo(OnlineStatus.DELETED);

        Optional<Product> productCheckOpt = productRepository.findById(product.getProductId());
        assertThat(productCheckOpt).isPresent();
        assertThat(productCheckOpt.get().getStatus()).isEqualTo(OnlineStatus.ACTIVE);

        Category categoryChecked = categoryRepository.getCategoryByIdAndFetchProducts(category.getCategoryId());
        assertThat(categoryChecked).isNotNull();
        assertThat(categoryChecked
                .getProducts()
                .stream()
                .noneMatch(prd -> productToDeleteId.equals(prd.getProductId())))
                .isTrue();
    }
}
