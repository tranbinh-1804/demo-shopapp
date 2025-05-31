package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.entities.ProductImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Image's name")
    private String imageUrl;

    public static ProductImageDTO fromEntity(ProductImage productImage) {
        return ProductImageDTO.builder()
                .id(productImage.getId())
                .productId(productImage.getProduct().getId())
                .imageUrl(productImage.getImageUrl())
                .build();
    }

    public ProductImage toEntity(Product product) {
        return ProductImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .product(product)
                .build();
    }
}
