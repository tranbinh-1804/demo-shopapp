package com.tranbinh.demo_shopapp.controllers;

import com.github.javafaker.Faker;
import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories); // Trả về danh sách category
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            return ResponseEntity.ok(categoryDTO); // Trả về DTO
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Trả về status 404 nếu không tìm thấy
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Xử lý lỗi khác
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        try {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory); // Trả về DTO đã tạo và status 201
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Xử lý lỗi chung nếu có
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category with id " + id + " deleted successfully"); // Trả về thông báo thành công
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Trả về status 404 nếu không tìm thấy
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Xử lý lỗi khác
        }
    }

    @PostMapping("/generateFakeCategories")
    private ResponseEntity<?> generateFakeCategories() {
        Faker faker = new Faker();
        List<CategoryDTO> createdCategories = new ArrayList<>();
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
                CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
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
