package com.umeal.api.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class OrderCreateDTO {
    @NotNull(message = "Restaurante é obrigatório.")
    private Long restaurantId;

    @NotNull(message = "Endereço de entrega é obrigatório.")
    private Long deliveryAddressId;

    @Valid
    @NotNull
    @Size(min = 1, message = "O pedido deve ter pelo menos um item.")
    private List<OrderItemCreateDTO> items;
}