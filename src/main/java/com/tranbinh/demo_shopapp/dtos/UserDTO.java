package com.tranbinh.demo_shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat; // Thêm import này
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phonenumber")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    // Sử dụng @JsonFormat để chỉ định định dạng cho Jackson
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth; // Thay đổi kiểu dữ liệu thành LocalDate

    @JsonProperty("facebook_id")
    private Integer facebookId;

    @JsonProperty("google_id")
    private Integer googleId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private int roleId;
}