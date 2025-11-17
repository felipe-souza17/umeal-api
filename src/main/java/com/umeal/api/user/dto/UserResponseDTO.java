package com.umeal.api.user.dto;

import com.umeal.api.user.model.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private UserRole role;
}
