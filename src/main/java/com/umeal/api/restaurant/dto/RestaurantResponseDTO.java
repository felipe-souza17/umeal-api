package com.umeal.api.restaurant.dto;

import com.umeal.api.address.dto.AddressResponseDTO;
import lombok.Data;
import java.util.Set;

@Data
public class RestaurantResponseDTO {
    private Long id;
    private String restaurantName;
    private String cnpj;
    private AddressResponseDTO address;
    private Set<CategoryDTO> categories;
}