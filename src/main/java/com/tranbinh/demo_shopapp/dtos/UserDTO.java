package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat; // Thêm import này
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tranbinh.demo_shopapp.entities.Role;
import com.tranbinh.demo_shopapp.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// Bỏ import org.springframework.format.annotation.DateTimeFormat; đi nếu không cần nữa
// import org.springframework.format.annotation.DateTimeFormat; // Có thể bỏ dòng này

import java.time.LocalDate; // Sử dụng LocalDate
// import java.util.Date; // Bỏ import này đi

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phonenumber")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    private String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "Retype password is required")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    // Sử dụng @JsonFormat để chỉ định định dạng cho Jackson
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth; // Thay đổi kiểu dữ liệu thành LocalDate

    @JsonProperty("facebook_id")
    private Integer facebookId;

    @JsonProperty("google_id")
    private Integer googleId;

    @JsonProperty("is_active")
    private boolean isActive;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;

    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth())
                .facebookId(user.getFacebookId())
                .googleId(user.getGoogleId())
                .isActive(user.getIsActive())
                .roleId(user.getRole().getId())
                .build();
    }

    public User toEntity() {
        Role role = Role.builder()
                .id(this.getRoleId())
                .build();
        return User.builder()
                .id(this.getId())
                .fullName(this.getFullName())
                .phoneNumber(this.getPhoneNumber())
                .address(this.getAddress())
                .email(this.getEmail())
                .username(this.getUsername())
                .password(this.password != null ? this.getPassword() : null)
                .dateOfBirth(this.getDateOfBirth())
                .facebookId(this.getFacebookId())
                .googleId(this.getGoogleId())
                .isActive(this.isActive())
                .role(role)
                .build();
    }
}