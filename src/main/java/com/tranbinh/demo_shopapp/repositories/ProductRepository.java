package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Kiểm tra xem sản phẩm có tồn tại theo tên không
    Boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);
}
