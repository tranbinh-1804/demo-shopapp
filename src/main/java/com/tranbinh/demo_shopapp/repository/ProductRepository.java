package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Kiểm tra xem sản phẩm có tồn tại theo tên không
    Boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);
}
