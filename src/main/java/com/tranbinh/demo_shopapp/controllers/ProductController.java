package com.tranbinh.demo_shopapp.controllers;

import com.github.javafaker.Faker;
import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.CategoryRepository;
import com.tranbinh.demo_shopapp.responses.ProductListResponse;
import com.tranbinh.demo_shopapp.responses.ProductResponse;
import com.tranbinh.demo_shopapp.services.ICategoryService;
import com.tranbinh.demo_shopapp.services.IProductService;
import com.tranbinh.demo_shopapp.validation.OnProductSubmission;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Random;

/**
 * REST controller for managing product-related operations.
 * Handles CRUD operations for products including listing, retrieval, creation,
 * updating and deletion of products.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;
    private final ICategoryService categoryService;

    /**
     * Retrieves a paginated list of products sorted by creation date.
     *
     * @param page  Page number (zero-based), defaults to 0
     * @param limit Maximum number of items per page, defaults to 10
     * @return ResponseEntity containing list of products and total pages
     */
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        List<ProductResponse> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        return ResponseEntity.ok(ProductListResponse.builder()
                .productResponses(products)
                .totalPages(totalPages)
                .build());
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return ResponseEntity containing the product if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Creates a new product with the provided details.
     * Handles multipart form data including product information and images.
     *
     * @param productDTO The product data transfer object containing product details
     * @param result     Validation result for the product data
     * @return ResponseEntity containing the created product or error messages
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Validated(OnProductSubmission.class) @ModelAttribute ProductDTO productDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            ProductDTO newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Error creating product: " + e.getMessage());
        }
    }

    /**
     * Updates an existing product with the provided details.
     * Handles multipart form data including updated product information and images.
     *
     * @param id         The ID of the product to update
     * @param productDTO The product data transfer object containing updated details
     * @param result     Validation result for the product data
     * @return ResponseEntity containing the updated product or error messages
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Validated(OnProductSubmission.class) @ModelAttribute ProductDTO productDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return ResponseEntity containing success message or error details
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product successfully with id = " + id);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting product: " + e.getMessage());
        }
    }

    @PostMapping("/generateFakeProduct")
    private ResponseEntity<?> generateFakeProduct() {
        Faker faker = new Faker();
        Random random = new Random();
        List<ProductDTO> createdProducts = new java.util.ArrayList<>();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        int numberOfProductToCreate = 1000;
        for (int i = 0; i < numberOfProductToCreate; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }

            CategoryDTO categoryDTO = categories.get(random.nextInt(categories.size()));
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) Math.round(faker.number().randomDouble(2, 10, 5000) * 100) / 100)
                    .description(faker.lorem().paragraph(2))
                    .category(categoryDTO)
                    .thumbnail("")
                    .build();
            try {
                productService.createProduct(productDTO);
                createdProducts.add(productDTO);
            } catch (DataIntegrityViolationException e) {
                System.err.println("Could not create fake product due to integrity violation (possibly duplicate name): " + productDTO.getName() + ". Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error creating fake product: " + productDTO.getName() + ". Error: " + e.getMessage());
                // Xử lý lỗi (ví dụ: ghi log)
            }
        }
        if (createdProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No fake products were created. Check logs for errors.");
        }
        return ResponseEntity.ok("Successfully generated " + createdProducts.size() + " fake products.");
    }
}