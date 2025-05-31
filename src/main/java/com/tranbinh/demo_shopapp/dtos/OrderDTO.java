package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tranbinh.demo_shopapp.entities.Order;
import com.tranbinh.demo_shopapp.entities.OrderStatus;
import com.tranbinh.demo_shopapp.entities.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phonenumber")
    @NotBlank(message = "Phone number is required")
    @Min(value = 9, message = "Phone number must be at least 9 characters")
    private String phoneNumber;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be greater than or equal to 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    public static OrderDTO fromEntity(Order order) {
        return OrderDTO.builder()
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .trackingNumber(order.getTrackingNumber())
                .build();
    }

    public Order toEntity(User user) {
        return Order.builder()
                .id(user.getId())
                .fullName(this.fullName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .note(this.note)
                .totalMoney(this.totalMoney)
                .orderDate(LocalDateTime.now())
                .shippingMethod(this.shippingMethod)
                .shippingAddress(this.shippingAddress)
                .shippingDate(this.shippingDate)
                .paymentMethod(this.paymentMethod)
                .status(this.status)
                .trackingNumber(this.trackingNumber)
                .active(true)
                .build();
    }
}
