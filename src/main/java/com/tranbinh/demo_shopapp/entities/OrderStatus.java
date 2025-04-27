package com.tranbinh.demo_shopapp.entities;

public enum OrderStatus {
    PENDING,     // Đơn hàng mới, chờ xử lý
    PROCESSING,  // Đang xử lý
    SHIPPED,     // Đã giao cho đơn vị vận chuyển
    DELIVERED,   // Đã giao hàng
    CANCELLED,   // Đã hủy
    RETURNED     // Hoàn trả
}