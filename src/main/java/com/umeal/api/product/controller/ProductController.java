package com.umeal.api.product.controller;

import com.umeal.api.product.dto.ProductCreateDTO;
import com.umeal.api.product.dto.ProductResponseDTO;
import com.umeal.api.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Vamos usar duas bases de rota nesta classe
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/restaurants/{restaurantId}/products")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @PathVariable Long restaurantId,
            @Valid @RequestBody ProductCreateDTO dto,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();
        ProductResponseDTO newProduct = productService.createProduct(dto, restaurantId, ownerEmail);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/restaurants/{restaurantId}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsFromRestaurant(
            @PathVariable Long restaurantId) {
        
        List<ProductResponseDTO> products = productService.getProductsForRestaurant(restaurantId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductCreateDTO dto,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();
        ProductResponseDTO updatedProduct = productService.updateProduct(productId, dto, ownerEmail);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId,
            Authentication authentication) {
        
        String ownerEmail = authentication.getName();
        productService.deleteProduct(productId, ownerEmail);
        return ResponseEntity.noContent().build();
    }
}