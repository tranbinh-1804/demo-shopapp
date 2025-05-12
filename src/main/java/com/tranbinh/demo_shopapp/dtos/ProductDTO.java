package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    @NotNull(message = "Product price cannot be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100, message = "Discount must be less than or equal to 100")
    private Float discount;

    private String thumbnail;

    private String description;

    @NotNull(message = "Category ID cannot be null")
    @JsonProperty("category_id")
    private Long categoryId;

    private List<MultipartFile> files;

    @JsonProperty("image_urls")
    private List<String> imageUrls;
}