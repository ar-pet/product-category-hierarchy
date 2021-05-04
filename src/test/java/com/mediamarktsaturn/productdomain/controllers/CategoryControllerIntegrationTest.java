package com.mediamarktsaturn.productdomain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.OnlineStatus;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.payload.CategoryRequest;
import com.mediamarktsaturn.productdomain.payload.CategoryResponse;
import com.mediamarktsaturn.productdomain.repositories.CategoryRepository;
import com.mediamarktsaturn.productdomain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void cleanDatabase() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void createCategory() throws Exception {
        final String categoryName = "someCategoryName";
        CategoryRequest categoryRequest = new CategoryRequest(categoryName, null);
        MvcResult mvcResult = mockMvc.perform(
                post("/productdomain/api/category")
                        .content(objectMapper.writeValueAsString(categoryRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryResponse categoryResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryResponse.class);
        assertThat(categoryResponse.getName()).isEqualTo(categoryName);
        assertThat(categoryResponse.getSuperCategoryId()).isNull();

        Category createdCategory = categoryRepository.findById(categoryResponse.getId()).get();
        assertThat(createdCategory.getName()).isEqualTo(categoryName);
    }

    @Test
    public void deleteCategory_withoutProducts_success() throws Exception {
        Category category = categoryRepository.save(new Category("someCategoryName"));

        mockMvc.perform(
                delete("/productdomain/api/category" + "/" + category.getCategoryId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(categoryRepository.findById(category.getCategoryId())).isNotPresent();
    }

    @Test
    public void deleteCategory_withProducts_success() throws Exception {
        Category categoryToDelete = categoryRepository.save(new Category("someCategoryName"));
        Category category = categoryRepository.save(new Category("otherCategoryName"));
        Product product = createProduct("productName", Set.of(categoryToDelete, category));

        mockMvc.perform(
                delete("/productdomain/api/category" + "/" + categoryToDelete.getCategoryId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(categoryRepository.findById(categoryToDelete.getCategoryId())).isNotPresent();
        assertThat(categoryRepository.findById(category.getCategoryId())).isPresent();
        assertThat(productRepository.findById(product.getProductId())).isPresent();
    }

    private Product createProduct(String name, Set<Category> categories) {
        Product product = new Product();
        product.setName(name);
        product.setStatus(OnlineStatus.ACTIVE);

        categories.forEach(product::addCategory);
        product.setCategories(categories);

        Product savedProduct = productRepository.save(product);
        categoryRepository.saveAll(categories);
        return savedProduct;
    }
}
