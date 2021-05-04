package com.mediamarktsaturn.productdomain.services;

import com.mediamarktsaturn.productdomain.converters.CategoryConverter;
import com.mediamarktsaturn.productdomain.exception.CategoryDeleteException;
import com.mediamarktsaturn.productdomain.exception.CategoryInvalidException;
import com.mediamarktsaturn.productdomain.exception.CategoryNotFoundException;
import com.mediamarktsaturn.productdomain.models.Category;
import com.mediamarktsaturn.productdomain.models.Product;
import com.mediamarktsaturn.productdomain.payload.CategoryRequest;
import com.mediamarktsaturn.productdomain.payload.CategoryResponse;
import com.mediamarktsaturn.productdomain.repositories.CategoryRepository;
import com.mediamarktsaturn.productdomain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryConverter categoryConverter;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
        this.productRepository = productRepository;
    }

    public CategoryResponse getCategoryResponseById(long id) {
        return categoryConverter.fromCategory(getCategoryById(id));
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRequest.getName() == null) {
            throw new CategoryInvalidException();
        }
        var category = new Category();
        category.setName(categoryRequest.getName());

        Long superCategoryId = categoryRequest.getSuperCategoryId();
        if (superCategoryId != null) {
            Optional<Category> superCategoryOpt = categoryRepository.findById(superCategoryId);
            if (superCategoryOpt.isPresent()) {
                category.setSuperCategory(superCategoryOpt.get());
            } else {
                throw new CategoryNotFoundException(superCategoryId);
            }
        }

        return categoryConverter.fromCategory(categoryRepository.save(category));
    }

    /**
     * Deletes the category with the given <code>id</code>
     * <p>Can not delete the category if there are subcategories attached to it.</p>
     * <p>Can not delete the category if any of its products does not have any other category.</p>
     *
     * @param id
     */
    @Transactional
    public void deleteCategory(long id) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!category.getSubCategories().isEmpty()) {
            throw new CategoryDeleteException(id);
        }

        categoryRepository.deleteById(id);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        if (categoryRequest.getName() == null || id.equals(categoryRequest.getSuperCategoryId())) {
            throw new CategoryInvalidException();
        }
        var sourceCategory = categoryRepository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        Long newSuperCategoryId = categoryRequest.getSuperCategoryId();
        if (newSuperCategoryId != null) {
            var newSuperCategory = categoryRepository
                    .findById(newSuperCategoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(newSuperCategoryId));
            sourceCategory.setSuperCategory(newSuperCategory);
        } else {
            sourceCategory.setSuperCategory(null);
        }

        sourceCategory.setName(categoryRequest.getName());
        return categoryConverter.fromCategory(categoryRepository.save(sourceCategory));
    }
}
