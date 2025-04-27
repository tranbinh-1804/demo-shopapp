package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Lấy thông tin của một oder detail
    OrderDetail findByOrderId(Long orderId);
}
