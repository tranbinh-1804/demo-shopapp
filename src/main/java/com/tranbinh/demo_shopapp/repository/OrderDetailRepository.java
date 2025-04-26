package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Lấy thông tin của một oder detail
    OrderDetail findByOrderId(Long orderId);
}
