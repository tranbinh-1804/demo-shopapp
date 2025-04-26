package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
