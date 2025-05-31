package com.tranbinh.demo_shopapp.dtos;

import com.tranbinh.demo_shopapp.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 25, message = "Role name must be between 3 and 25 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    public static RoleDTO fromEntity(Role role) {
        if (role == null) {
            return null;
        }

        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }
}
