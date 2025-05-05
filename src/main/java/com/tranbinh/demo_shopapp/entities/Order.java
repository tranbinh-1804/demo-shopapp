package com.tranbinh.demo_shopapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Builder
@Data

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "fullname", length = 255, nullable = false)
    private String fullName;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "phonenumber", length = 15, nullable = false)
    private String phoneNumber;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "shipping_address", length = 255, nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "shipping_status", length = 20)
    private String shippingStatus;

    @Column(name = "tracking_number", length = 50)
    private String trackingNumber;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;
}
