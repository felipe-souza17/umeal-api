package com.umeal.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String name;


}