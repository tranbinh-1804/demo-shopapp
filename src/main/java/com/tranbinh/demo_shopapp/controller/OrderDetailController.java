package com.tranbinh.demo_shopapp.controller;

import com.tranbinh.demo_shopapp.dto.OrderDTO;
import com.tranbinh.demo_shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    // Thêm mới một oder detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO newOderDetail) {
        return ResponseEntity.ok("Create order detail successfully:\n" + newOderDetail);
    }

    // Lấy thông tin của một oder detail
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok("Get order detail with id = " + id);
    }

    // Lấy thông tin của các order detail của một order id
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("order_id") Long order_id) {
        return ResponseEntity.ok("Get order detail with order id = " + order_id);
    }

    // Cập nhật order detail
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") Long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("Update order detail successfully with id = " + id + "\n" + orderDetailDTO);
    }

    // Xóa order detail
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Delete order detail successfully with id = " + id);
    }
}
