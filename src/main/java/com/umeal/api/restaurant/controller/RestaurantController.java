package com.umeal.api.restaurant.controller;

import com.umeal.api.order.dto.OrderResponseDTO;
import com.umeal.api.order.service.OrderService;
import com.umeal.api.restaurant.dto.RestaurantCreateDTO;
import com.umeal.api.restaurant.dto.RestaurantResponseDTO;
import com.umeal.api.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes.")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Cria um novo restaurante", description = "Rota protegida para criar um novo restaurante.")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateDTO dto,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();

        RestaurantResponseDTO savedRestaurantDTO = restaurantService.createRestaurant(dto, ownerEmail);

        return new ResponseEntity<>(savedRestaurantDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todos os restaurantes", description = "Rota pública para listar todos os restaurantes.")
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        List<RestaurantResponseDTO> restaurants = restaurantService.listAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }


    @GetMapping("/{restaurantId}")
    @Operation(summary = "Busca detalhes de um restaurante por ID", description = "Rota pública para obter os detalhes de um restaurante específico.")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @PathVariable Long restaurantId) {
        RestaurantResponseDTO restaurant = restaurantService.getRestaurantDetails(restaurantId);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersForRestaurant(
            @PathVariable Long restaurantId,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();
        List<OrderResponseDTO> orders = orderService.getOrdersForRestaurant(restaurantId, ownerEmail);
        return ResponseEntity.ok(orders);
    }
}