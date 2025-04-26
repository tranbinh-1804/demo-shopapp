package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Lấy danh sách đơn hàng của một người dùng
    List<Order> findByUserId(Long userId);
}
