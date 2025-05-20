package com.tranbinh.demo_shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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


}
