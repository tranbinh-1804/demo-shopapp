package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
