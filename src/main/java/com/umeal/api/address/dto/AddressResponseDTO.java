package com.umeal.api.address.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
}