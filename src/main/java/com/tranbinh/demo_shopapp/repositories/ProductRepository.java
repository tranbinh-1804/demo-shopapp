package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Thêm phương thức này
    @EntityGraph(attributePaths = {"productImages"})
    Page<Product> findAll(Pageable pageable);

    boolean existsByName(String name);

    @EntityGraph(attributePaths = {"productImages"})
    Optional<Product> findById(Long id);
}