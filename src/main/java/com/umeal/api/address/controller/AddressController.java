package com.umeal.api.address.controller;

import com.umeal.api.address.dto.AddressCreateDTO;
import com.umeal.api.address.dto.AddressResponseDTO;
import com.umeal.api.address.dto.ViaCepResponseDTO;
import com.umeal.api.address.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(
            @Valid @RequestBody AddressCreateDTO dto,
            Authentication authentication) {
        
        String email = authentication.getName();
        AddressResponseDTO createdAddress = addressService.createAddress(dto, email);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<AddressResponseDTO>> getMyAddresses(Authentication authentication) {
        String email = authentication.getName();
        List<AddressResponseDTO> addresses = addressService.getAddressesForUser(email);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepResponseDTO> getAddressByCep(@PathVariable String cep) {
        ViaCepResponseDTO addressInfo = addressService.getAddressFromViaCep(cep);
        return ResponseEntity.ok(addressInfo);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressCreateDTO dto,
            Authentication authentication) {
        
        String email = authentication.getName();
        AddressResponseDTO updatedAddress = addressService.updateAddress(addressId, dto, email);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        
        String email = authentication.getName();
        addressService.deleteAddress(addressId, email);
        return ResponseEntity.noContent().build();
    }
}