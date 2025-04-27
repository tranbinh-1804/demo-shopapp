package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @NotNull(message = "Order ID is required")
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order ID must be greater than or equal to 1")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID must be greater than or equal to 1")
    private Long productId;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Long price;

    @NotNull(message = "Number of products is required")
    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Number of products must be greater than or equal to 1")
    private int numberOfProducts;

    @NotNull(message = "Total money is required")
    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be greater than or equal to 0")
    private Long totalMoney;

    // Color có thể để optional vì không phải sản phẩm nào cũng có màu sắc
    private String color;
}
