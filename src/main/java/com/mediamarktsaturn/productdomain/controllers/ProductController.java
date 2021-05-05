package com.mediamarktsaturn.productdomain.controllers;

import com.mediamarktsaturn.productdomain.payload.ProductRequest;
import com.mediamarktsaturn.productdomain.payload.ProductResponse;
import com.mediamarktsaturn.productdomain.services.ProductService;
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

import java.util.Set;

@RestController
@RequestMapping("/productdomain/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Deletes the specified product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The product was successfully deleted for the given ID"),
            @ApiResponse(code = 404, message = "No product found with the given ID")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
    }

    @ApiOperation(value = "Creates a new product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The product was successfully created"),
            @ApiResponse(code = 400, message = "Provided product data is invalid"),
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        final ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @ApiOperation(value = "Returns the full category path of the product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category path was successfully returned"),
            @ApiResponse(code = 400, message = "No product found with the given ID")
    })
    @GetMapping("/{id}/categorypath")
    public ResponseEntity<Set<String>> getCategoryPath(@PathVariable long id) {
        return ResponseEntity.ok(productService.getCategoryPaths(id));
    }

    @ApiOperation(value = "Returns the product with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The product was successfully returned with the given ID"),
            @ApiResponse(code = 400, message = "No product found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductResponseById(id));
    }

    @ApiOperation(value = "Updates the product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The product was successfully updated"),
            @ApiResponse(code = 404, message = "No product found with the given ID"),
            @ApiResponse(code = 400, message = "The product can not be updated"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable long id,
                                                           @RequestBody ProductRequest productRequest) {
        final ProductResponse productResponse = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(productResponse);
    }
}
