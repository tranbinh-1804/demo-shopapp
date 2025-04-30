package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.UserDTO;
import com.tranbinh.demo_shopapp.entities.Role;
import com.tranbinh.demo_shopapp.entities.User;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.RoleRepository;
import com.tranbinh.demo_shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException, DataIntegrityViolationException, Exception {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

            Role role = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new DataNotFoundException("Role not found"));

            User newUser = User.builder()
                    .fullName(userDTO.getFullName())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .address(userDTO.getAddress())
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .facebookId(userDTO.getFacebookId())
                    .googleId(userDTO.getGoogleId())
                    .isActive(true)
                    .role(role)
                    .build();
            return userRepository.save(newUser);
    }

    @Override
    public String login(String username, String password) throws Exception {
        return "";
    }
}
