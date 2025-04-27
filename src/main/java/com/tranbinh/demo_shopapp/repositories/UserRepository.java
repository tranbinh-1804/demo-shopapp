package com.tranbinh.demo_shopapp.repositories;

import com.tranbinh.demo_shopapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNumber);

    User findByUsername(String username);
}
