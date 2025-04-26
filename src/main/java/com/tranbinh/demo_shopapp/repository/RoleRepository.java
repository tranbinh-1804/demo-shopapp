package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
