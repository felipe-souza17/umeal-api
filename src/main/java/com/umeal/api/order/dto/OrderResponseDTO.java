package com.umeal.api.order.dto;

import com.umeal.api.address.dto.AddressResponseDTO;
import com.umeal.api.order.model.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String restaurantName;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private AddressResponseDTO deliveryAddress;
    private List<OrderItemResponseDTO> items;
}