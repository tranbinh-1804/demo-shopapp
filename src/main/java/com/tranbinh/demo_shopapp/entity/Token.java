package com.tranbinh.demo_shopapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @Column(name = "token_type", nullable = false, length = 255)
    private String tokenType;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
