package com.tranbinh.demo_shopapp.repository;

import com.tranbinh.demo_shopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNumber);

    User findByUsername(String username);
}
