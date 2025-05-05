package com.tranbinh.demo_shopapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
@Data
public class Role {
    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 25)
    private String name;
}
