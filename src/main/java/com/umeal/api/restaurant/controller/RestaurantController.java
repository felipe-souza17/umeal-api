package com.umeal.api.restaurant.controller;

import com.umeal.api.restaurant.dto.RestaurantCreateDTO;
import com.umeal.api.restaurant.dto.RestaurantResponseDTO;
import com.umeal.api.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateDTO dto,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();

        RestaurantResponseDTO savedRestaurantDTO = restaurantService.createRestaurant(dto, ownerEmail);

        return new ResponseEntity<>(savedRestaurantDTO, HttpStatus.CREATED);
    }
}