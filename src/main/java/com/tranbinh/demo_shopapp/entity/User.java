package com.tranbinh.demo_shopapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", length = 255, nullable = false)
    private String fullName;

    @Column(name = "phonenumber", length = 15, nullable = false)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "username", length = 255, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_id")
    private Integer facebookId;

    @Column(name = "google_id")
    private Integer googleId;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
