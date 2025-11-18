package com.umeal.api.user.controller;

import com.umeal.api.user.dto.UserCreateDTO;
import com.umeal.api.user.dto.UserResponseDTO;
import com.umeal.api.user.dto.UserUpdateDTO;
import com.umeal.api.user.model.User;
import com.umeal.api.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para cadastro e gerenciamento de perfil.")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register-client")
    @Operation(summary = "Registra um novo Cliente", description = "Rota pública para cadastrar um usuário com a role CLIENT.")
    public ResponseEntity<UserResponseDTO> registerClient(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User savedUser = userService.registerClient(userCreateDTO);
        
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setName(savedUser.getName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/register-owner")
    @Operation(summary = "Registra um novo Dono de Restaurante", description = "Rota pública para cadastrar um usuário com a role RESTAURANT_OWNER.")
    public ResponseEntity<UserResponseDTO> registerOwner(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User savedUser = userService.registerOwner(userCreateDTO);
        
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setName(savedUser.getName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Busca o perfil do usuário logado", description = "Rota protegida. Retorna os dados do usuário (cliente ou dono) atrelado ao token JWT.")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();

        UserResponseDTO userProfile = userService.getUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        
        String email = authentication.getName();
        
        UserResponseDTO updatedUser = userService.updateUserProfile(email, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}