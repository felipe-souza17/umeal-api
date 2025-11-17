package com.umeal.api.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddressCreateDTO {

    @NotBlank(message = "A rua é obrigatória.")
    private String street;

    private String number;
    private String complement;

    @NotBlank(message = "O bairro é obrigatório.")
    private String neighborhood;

    @NotBlank(message = "A cidade é obrigatória.")
    private String city;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres (ex: SP).")
    private String state;

    @NotBlank(message = "O CEP é obrigatório.")
    private String zipCode;

    @NotNull(message = "A latitude é obrigatória.")
    private BigDecimal latitude;

    @NotNull(message = "A longitude é obrigatória.")
    private BigDecimal longitude;
}