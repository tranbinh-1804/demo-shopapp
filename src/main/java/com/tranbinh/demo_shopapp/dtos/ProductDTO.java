package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.entities.ProductImage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
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

    @JsonProperty("category")
    @NotNull(message = "Category infomation cannot be null")
    @Valid
    private CategoryDTO category;

    private List<MultipartFile> files;

    @JsonProperty("image_urls")
    private List<String> imageUrls;

    public static ProductDTO fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        CategoryDTO categoryDTO = null;
        if (product.getCategory() != null) {
            categoryDTO = CategoryDTO.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .build();
        }

        List<String> imageUrls = product.getProductImages() != null ?
                product.getProductImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList() : new ArrayList<>();

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .category(categoryDTO)
                .imageUrls(imageUrls)
                .build();
    }

    public Product toEntity(Category category) {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .thumbnail(this.thumbnail)
                .category(category)
                .build();
    }
}