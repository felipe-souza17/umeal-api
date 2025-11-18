package com.umeal.api.order.controller;

import com.umeal.api.order.dto.OrderCreateDTO;
import com.umeal.api.order.dto.OrderResponseDTO;
import com.umeal.api.order.dto.OrderStatusUpdateDTO;
import com.umeal.api.order.service.OrderService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos.")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Rota protegida para criar um novo pedido.")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderCreateDTO dto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        OrderResponseDTO newOrderDTO = orderService.createOrder(dto, userEmail);

        return new ResponseEntity<>(newOrderDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista meus pedidos", description = "Rota protegida para listar meus pedidos.")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderResponseDTO> orders = orderService.getOrdersForUser(userEmail);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Atualiza o status de um pedido", description = "Rota protegida para atualizar o status de um pedido.")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateDTO dto,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, dto.getStatus(), ownerEmail);
        return ResponseEntity.ok(updatedOrder);
    }
}