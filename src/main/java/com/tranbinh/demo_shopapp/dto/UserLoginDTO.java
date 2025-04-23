package com.tranbinh.demo_shopapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {
    private String username;
    private String password;
}
