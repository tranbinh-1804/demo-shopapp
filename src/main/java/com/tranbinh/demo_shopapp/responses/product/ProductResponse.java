package com.tranbinh.demo_shopapp.responses.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.responses.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonPropertyOrder({
        "id",
        "name",
        "price",
        "thumbnail",
        "description",
        "created_at",
        "updated_at",
        "category_name"
})
public class ProductResponse extends BaseResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("description")
    private String description;

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryName(product.getCategory().getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

}
