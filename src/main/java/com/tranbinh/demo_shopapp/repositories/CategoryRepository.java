package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}
