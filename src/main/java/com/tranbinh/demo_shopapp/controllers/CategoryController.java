package com.tranbinh.demo_shopapp.controllers;

import com.github.javafaker.Faker;
import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.responses.ApiResponse;
import com.tranbinh.demo_shopapp.responses.category.CategoryResponse;
import com.tranbinh.demo_shopapp.services.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(CategoryResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(categoryResponses, "Get all categories successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid category ID", HttpStatus.BAD_REQUEST));
        }
        try {
            Category category = categoryService.getCategoryById(id);
            CategoryResponse categoryResponse = CategoryResponse.fromEntity(category);
            return ResponseEntity.ok(ApiResponse.success(categoryResponse, "Category found successfully."));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR)); // Xử lý lỗi khác
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Validation failed", HttpStatus.BAD_REQUEST, errorMessages)
            );
        }
        try {
            Category createdCategory = categoryService.createCategory(categoryDTO);
            CategoryResponse categoryResponse = CategoryResponse.fromEntity(createdCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.created(categoryResponse, "Category created successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("Internal server error: " +
                            e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid category ID", HttpStatus.BAD_REQUEST));
        }

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
        }
        try {
            Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
            CategoryResponse categoryResponse = CategoryResponse.fromEntity(updatedCategory);
            return ResponseEntity.ok(ApiResponse.success(
                    categoryResponse, "Category updated successfully with id = " + id + "."));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid category ID", HttpStatus.BAD_REQUEST));
        }
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success(
                    "Category id: " + id + "has been deleted.",
                    "Category deleted successfully.")); // Trả về thông báo thành công
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND)); // Trả về status 404 nếu không tìm thấy
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("Internal server error: " +
                            e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)); // Xử lý lỗi khác
        }
    }

    @PostMapping("/generateFakeCategories")
    private ResponseEntity<?> generateFakeCategories() {
        Faker faker = new Faker();
        List<Category> createdCategories = new ArrayList<>();
        int numberOfCategoriesToCreate = 10; // Bạn có thể thay đổi số lượng ở đây

        for (int i = 0; i < numberOfCategoriesToCreate; i++) {
            String originalCategoryName = faker.commerce().department(); // Tạo tên category ngẫu nhiên
            // Kiểm tra xem tên category đã tồn tại chưa để tránh lỗi DataIntegrityViolationException
            // Đây là một cách đơn giản, bạn có thể tối ưu hóa bằng cách kiểm tra trong service nếu cần
            boolean nameExists = categoryService.getAllCategories().stream()
                    .anyMatch(cat -> cat.getName().equalsIgnoreCase(originalCategoryName));
            String categoryName;
            if (nameExists) {
                continue;
            } else {
                categoryName = originalCategoryName;
            }

            CategoryDTO categoryDTO = CategoryDTO.builder()
                    .name(categoryName)
                    .build();
            try {
                Category createdCategory = categoryService.createCategory(categoryDTO);
                createdCategories.add(createdCategory);
            } catch (DataIntegrityViolationException e) {
                System.err.println("Could not create fake category due to integrity violation (possibly duplicate name): " + categoryName + ". Error: " + e.getMessage());
                // Bỏ qua lỗi này và tiếp tục hoặc xử lý theo cách khác
            } catch (Exception e) {
                System.err.println("Error creating fake category: " + categoryName + ". Error: " + e.getMessage());
                // Xử lý lỗi (ví dụ: ghi log)
            }
        }
        if (createdCategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No fake categories were created. Check logs for errors.");
        }
        return ResponseEntity.ok("Successfully generated " + createdCategories.size() + " fake categories: " + createdCategories);
    }
}
