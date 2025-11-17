package com.umeal.api.address.service;

import com.umeal.api.address.dto.AddressCreateDTO;
import com.umeal.api.address.dto.AddressResponseDTO;
import com.umeal.api.address.model.Address;
import com.umeal.api.address.repository.AddressRepository;
import com.umeal.api.exception.AccessForbiddenException;
import com.umeal.api.exception.ResourceNotFoundException;
import com.umeal.api.user.model.User;
import com.umeal.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private AddressResponseDTO mapToAddressResponseDTO(Address address) {
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

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", email));
    }
    
    private Address findAddressAndCheckOwnership(Long addressId, Integer userId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço", addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new AccessForbiddenException("Você não tem permissão para modificar este endereço.");
        }
        
        return address;
    }

    @Transactional
    public AddressResponseDTO createAddress(AddressCreateDTO dto, String userEmail) {
        User user = getUserByEmail(userEmail);

        Address newAddress = new Address();
        newAddress.setStreet(dto.getStreet());
        newAddress.setNumber(dto.getNumber());
        newAddress.setComplement(dto.getComplement());
        newAddress.setNeighborhood(dto.getNeighborhood());
        newAddress.setCity(dto.getCity());
        newAddress.setState(dto.getState());
        newAddress.setZipCode(dto.getZipCode());
        newAddress.setLatitude(dto.getLatitude());
        newAddress.setLongitude(dto.getLongitude());
        newAddress.setUser(user);

        Address savedAddress = addressRepository.save(newAddress);
        return mapToAddressResponseDTO(savedAddress);
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getAddressesForUser(String userEmail) {
        User user = getUserByEmail(userEmail);
        
        return addressRepository.findByUser(user)
                .stream()
                .map(this::mapToAddressResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponseDTO updateAddress(Long addressId, AddressCreateDTO dto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = findAddressAndCheckOwnership(addressId, user.getId());

        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setComplement(dto.getComplement());
        address.setNeighborhood(dto.getNeighborhood());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());

        Address updatedAddress = addressRepository.save(address);
        return mapToAddressResponseDTO(updatedAddress);
    }

    @Transactional
    public void deleteAddress(Long addressId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = findAddressAndCheckOwnership(addressId, user.getId());

        addressRepository.delete(address);
    }
}