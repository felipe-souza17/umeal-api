package com.umeal.api.product.service;

import com.umeal.api.exception.AccessForbiddenException;
import com.umeal.api.exception.ResourceNotFoundException;
import com.umeal.api.product.dto.ProductCreateDTO;
import com.umeal.api.product.dto.ProductResponseDTO;
import com.umeal.api.product.model.Product;
import com.umeal.api.product.repository.ProductRepository;
import com.umeal.api.restaurant.model.Restaurant;
import com.umeal.api.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }

    private Restaurant findRestaurantAndCheckOwnership(Long restaurantId, String ownerEmail) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", restaurantId));

        if (!restaurant.getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessForbiddenException("Você não é o dono deste restaurante.");
        }
        return restaurant;
    }
    
    private Product findProductAndCheckOwnership(Long productId, String ownerEmail) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto", productId));
            
        if (!product.getRestaurant().getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessForbiddenException("Você não tem permissão para modificar este produto.");
        }
        return product;
    }


    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto, Long restaurantId, String ownerEmail) {
        Restaurant restaurant = findRestaurantAndCheckOwnership(restaurantId, ownerEmail);

        Product newProduct = new Product();
        newProduct.setName(dto.getName());
        newProduct.setDescription(dto.getDescription());
        newProduct.setPrice(dto.getPrice());
        newProduct.setImageUrl(dto.getImageUrl());
        newProduct.setRestaurant(restaurant);

        Product savedProduct = productRepository.save(newProduct);
        return mapToProductResponseDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsForRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurante", restaurantId);
        }

        List<Product> products = productRepository.findByRestaurantId(restaurantId);
        return products.stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductCreateDTO dto, String ownerEmail) {
        Product product = findProductAndCheckOwnership(productId, ownerEmail);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponseDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId, String ownerEmail) {
        Product product = findProductAndCheckOwnership(productId, ownerEmail);
        
        productRepository.delete(product);
    }
}