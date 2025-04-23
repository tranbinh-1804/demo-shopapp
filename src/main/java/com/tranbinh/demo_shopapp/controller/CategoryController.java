package com.tranbinh.demo_shopapp.controller;

import com.tranbinh.demo_shopapp.dto.CategoryDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getCategories(@RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(String.format("Get categories successfully with page = %d, limit = %d", page, limit));
    }
    @PostMapping("")
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.ok("Create category successfully with name: " + categoryDTO.getName());
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Update category successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Delete category successfully");
    }
}
