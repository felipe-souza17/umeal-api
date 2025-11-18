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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Endereços", description = "Endpoints para gerenciamento de endereços.")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    @Operation(summary = "Cria um novo endereço", description = "Rota protegida para criar um novo endereço.")
    public ResponseEntity<AddressResponseDTO> createAddress(
            @Valid @RequestBody AddressCreateDTO dto,
            Authentication authentication) {
        
        String email = authentication.getName();
        AddressResponseDTO createdAddress = addressService.createAddress(dto, email);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Lista meus endereços", description = "Rota protegida para listar meus endereços.")
    public ResponseEntity<List<AddressResponseDTO>> getMyAddresses(Authentication authentication) {
        String email = authentication.getName();
        List<AddressResponseDTO> addresses = addressService.getAddressesForUser(email);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/cep/{cep}")
    @Operation(summary = "Busca endereço por CEP", description = "Rota pública para buscar informações de endereço usando o serviço ViaCEP.")
    public ResponseEntity<ViaCepResponseDTO> getAddressByCep(@PathVariable String cep) {
        ViaCepResponseDTO addressInfo = addressService.getAddressFromViaCep(cep);
        return ResponseEntity.ok(addressInfo);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Atualiza um endereço", description = "Rota protegida para atualizar um endereço.")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressCreateDTO dto,
            Authentication authentication) {
        
        String email = authentication.getName();
        AddressResponseDTO updatedAddress = addressService.updateAddress(addressId, dto, email);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Deleta um endereço", description = "Rota protegida para deletar um endereço.")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        
        String email = authentication.getName();
        addressService.deleteAddress(addressId, email);
        return ResponseEntity.noContent().build();
    }
}