package com.umeal.api.user.controller;

import com.umeal.api.user.dto.UserCreateDTO;
import com.umeal.api.user.dto.UserResponseDTO;
import com.umeal.api.user.model.User;
import com.umeal.api.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User savedUser = userService.registerUser(userCreateDTO);
        
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setName(savedUser.getName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}