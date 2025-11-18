package com.umeal.api.order.controller;

import com.umeal.api.order.dto.OrderCreateDTO;
import com.umeal.api.order.dto.OrderResponseDTO;
import com.umeal.api.order.service.OrderService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderCreateDTO dto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        OrderResponseDTO newOrderDTO = orderService.createOrder(dto, userEmail);

        return new ResponseEntity<>(newOrderDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderResponseDTO> orders = orderService.getOrdersForUser(userEmail);
        return ResponseEntity.ok(orders);
    }
}