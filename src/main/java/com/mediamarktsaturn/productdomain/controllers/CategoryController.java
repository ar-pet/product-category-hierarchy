package com.mediamarktsaturn.productdomain.controllers;

import com.mediamarktsaturn.productdomain.payload.CategoryRequest;
import com.mediamarktsaturn.productdomain.payload.CategoryResponse;
import com.mediamarktsaturn.productdomain.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productdomain/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Returns the category with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was successfully found for the given ID"),
            @ApiResponse(code = 404, message = "No category could be found for the given ID"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable long id) {
        final CategoryResponse categoryResponse = categoryService.getCategoryResponseById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @ApiOperation(value = "Creates a new category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was successfully created"),
            @ApiResponse(code = 400, message = "Provided category data is invalid"),
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        final CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @ApiOperation(value = "Deletes a specified category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was successfully deleted for the given ID"),
            @ApiResponse(code = 404, message = "No category found with the given ID"),
            @ApiResponse(code = 400, message = "The category can not be deleted because of subcategories or products"),
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
    }

    @ApiOperation(value = "Updates the category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was successfully updated"),
            @ApiResponse(code = 404, message = "No category found with the given ID"),
            @ApiResponse(code = 400, message = "The category can not be updated"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable long id,
                                                           @RequestBody CategoryRequest categoryRequest) {
        final CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }
}
