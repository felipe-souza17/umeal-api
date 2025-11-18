package com.umeal.api.restaurant.service;

import com.umeal.api.address.dto.AddressResponseDTO;
import com.umeal.api.address.model.Address;
import com.umeal.api.address.repository.AddressRepository;
import com.umeal.api.category.model.Category;
import com.umeal.api.category.repository.CategoryRepository;
import com.umeal.api.exception.AccessForbiddenException;
import com.umeal.api.exception.BusinessException;
import com.umeal.api.exception.ResourceNotFoundException;
import com.umeal.api.restaurant.dto.CategoryDTO;
import com.umeal.api.restaurant.dto.RestaurantCreateDTO;
import com.umeal.api.restaurant.dto.RestaurantResponseDTO;
import com.umeal.api.restaurant.model.Restaurant;
import com.umeal.api.restaurant.repository.RestaurantRepository;
import com.umeal.api.user.model.User;
import com.umeal.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    
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

    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    private RestaurantResponseDTO mapToRestaurantResponseDTO(Restaurant restaurant) {
        RestaurantResponseDTO dto = new RestaurantResponseDTO();
        dto.setId(restaurant.getId());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setCnpj(restaurant.getCnpj());

        dto.setAddress(mapToAddressDTO(restaurant.getAddress()));


        Set<CategoryDTO> categoryDTOs = restaurant.getCategories().stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toSet());
        dto.setCategories(categoryDTOs);

        return dto;
    }

    @Transactional
    public RestaurantResponseDTO createRestaurant(RestaurantCreateDTO dto, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", ownerEmail));

        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", dto.getAddressId()));

        if (address.getUser() == null || !address.getUser().getId().equals(owner.getId())) {
            throw new AccessForbiddenException("Este endereço não pertence ao usuário logado.");
        }
        
        if (address.getRestaurant() != null) {
            throw new BusinessException("Este endereço já está vinculado a outro restaurante.");
        }
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
        if (categories.size() != dto.getCategoryIds().size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas.", dto.getCategoryIds());
        }

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setRestaurantName(dto.getRestaurantName());
        newRestaurant.setCnpj(dto.getCnpj());
        newRestaurant.setOwner(owner);
        newRestaurant.setAddress(address);
        newRestaurant.setCategories(categories);
        
        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        return mapToRestaurantResponseDTO(savedRestaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> listAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::mapToRestaurantResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public RestaurantResponseDTO getRestaurantDetails(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", restaurantId));
        
        return mapToRestaurantResponseDTO(restaurant);
    }

}