package com.umeal.api.order.dto;

import com.umeal.api.order.model.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {

    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}