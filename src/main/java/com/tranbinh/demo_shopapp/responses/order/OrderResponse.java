package com.tranbinh.demo_shopapp.responses.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tranbinh.demo_shopapp.dtos.OrderDTO;
import com.tranbinh.demo_shopapp.entities.Order;
import com.tranbinh.demo_shopapp.entities.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_date")
    @JsonFormat(pattern = "dd-MM-yyyy' T 'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime orderDate;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse fromEntity(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetail();
        List<OrderDetailResponse> orderDetailResponses = null;
        if (orderDetails != null) {
            orderDetailResponses = orderDetails
                    .stream()
                    .map(OrderDetailResponse::fromOrderDetail)
                    .toList();
        }
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus().toString())
                .orderDate(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .trackingNumber(order.getTrackingNumber())
                .orderDetails(orderDetailResponses)
                .build();
    }

    public static OrderResponse fromDTO(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        return OrderResponse.builder()
                .id(null)
                .userId(dto.getUserId())
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .note(dto.getNote())
                .status(dto.getStatus() != null ? dto.getStatus().toString() : null)
                .totalMoney(dto.getTotalMoney())
                .shippingMethod(dto.getShippingMethod())
                .shippingAddress(dto.getShippingAddress())
                .shippingDate(dto.getShippingDate())
                .paymentMethod(dto.getPaymentMethod())
                .trackingNumber(dto.getTrackingNumber())
                .build();
    }
}
