package com.umeal.api.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.Set;

@Data
public class RestaurantCreateDTO {

    @NotBlank(message = "O nome do restaurante é obrigatório.")
    private String restaurantName;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @CNPJ(message = "CNPJ inválido.")
    private String cnpj;

    @NotNull(message = "O endereço é obrigatório.")
    private Long addressId;

    @NotNull(message = "O restaurante deve ter pelo menos uma categoria.")
    @Size(min = 1, message = "Informe ao menos uma categoria.")
    private Set<Long> categoryIds;
}