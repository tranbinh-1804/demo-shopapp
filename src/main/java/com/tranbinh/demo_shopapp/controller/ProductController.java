package com.tranbinh.demo_shopapp.controller;

import com.tranbinh.demo_shopapp.dto.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<String> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(String.format("Get products successfully with page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok("Get product with id = " + id);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO
    ) {
        try {
            List<MultipartFile> files = productDTO.getFiles();
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        // Kiểm tra kích thước file
                        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                            return ResponseEntity
                                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                    .body("File size exceeds the limit of 10MB");
                        }
                        // Kiểm tra loại file
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            return ResponseEntity
                                    .status(HttpStatus.BAD_REQUEST)
                                    .body("Invalid file type. Only image files are allowed.");
                        }
                        // Lưu file
                        String fileName = storeFile(file);
                        // Có thể thêm xử lý fileName ở đây nếu cần
                    }
                }
            }
            // Xử lý logic tạo sản phẩm ở đây
            return ResponseEntity.ok("Create product successfully with name: " + productDTO.getName());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error creating product: " + e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Tạo tên file ngẫu nhiên để tránh trùng lặp
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        // Tạo thư mục uploads nếu chưa tồn tại
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Lưu file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id) {
        return ResponseEntity.ok("Update product successfully with id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok("Delete product successfully with id = " + id);
    }
}