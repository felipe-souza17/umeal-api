package com.umeal.api.order.service;

import com.umeal.api.address.dto.AddressResponseDTO;
import com.umeal.api.address.model.Address;
import com.umeal.api.address.repository.AddressRepository;
import com.umeal.api.exception.AccessForbiddenException;
import com.umeal.api.exception.BusinessException;
import com.umeal.api.exception.ResourceNotFoundException;
import com.umeal.api.order.dto.*;
import com.umeal.api.order.model.Order;
import com.umeal.api.order.model.OrderItem;
import com.umeal.api.order.model.OrderStatus;
import com.umeal.api.order.repository.OrderRepository;
import com.umeal.api.product.model.Product;
import com.umeal.api.product.repository.ProductRepository;
import com.umeal.api.restaurant.model.Restaurant;
import com.umeal.api.restaurant.repository.RestaurantRepository;
import com.umeal.api.user.model.User;
import com.umeal.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;

    private AddressResponseDTO mapToAddressDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setNeighborhood(address.getNeighborhood());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        return dto;
    }

    private OrderItemResponseDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        
        dto.setRestaurantName(order.getRestaurant().getRestaurantName());
        
        dto.setDeliveryAddress(mapToAddressDTO(order.getDeliveryAddress()));

        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(this::mapToOrderItemDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }


    @Transactional
    public OrderResponseDTO createOrder(OrderCreateDTO dto, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userEmail));
        
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", dto.getRestaurantId()));
        
        Address address = addressRepository.findById(dto.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", dto.getDeliveryAddressId()));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessForbiddenException("Este endereço de entrega não pertence a você.");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        List<Long> productIds = dto.getItems().stream()
                                  .map(OrderItemCreateDTO::getProductId)
                                  .toList();
        
        Map<Long, Product> productsMap = productRepository.findAllById(productIds)
                                            .stream()
                                            .collect(Collectors.toMap(Product::getId, product -> product));

        for (OrderItemCreateDTO itemDto : dto.getItems()) {
            Product product = productsMap.get(itemDto.getProductId());

            if (product == null) {
                throw new ResourceNotFoundException("Produto", itemDto.getProductId());
            }
            if (!product.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BusinessException("Produto " + product.getName() + " não pertence ao restaurante " + restaurant.getRestaurantName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            orderItems.add(orderItem);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setRestaurant(restaurant);
        newOrder.setDeliveryAddress(address);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);

        for (OrderItem item : orderItems) {
            item.setOrder(newOrder);
        }
        
        newOrder.setItems(orderItems);

        Order savedOrder = orderRepository.saveAndFlush(newOrder);
        
        return mapToOrderResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userEmail));


        List<Order> orders = orderRepository.findByUserId(user.getId());

        return orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersForRestaurant(Long restaurantId, String ownerEmail) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", restaurantId));

        if (!restaurant.getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessForbiddenException("Você não tem permissão para ver os pedidos deste restaurante.");
        }

        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);

        return orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }
}