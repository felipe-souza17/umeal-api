package com.umeal.api.user.service;

import com.umeal.api.exception.user.EmailAlreadyExistsException;
import com.umeal.api.user.dto.UserCreateDTO;
import com.umeal.api.user.model.User;
import com.umeal.api.user.model.UserRole;
import com.umeal.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserCreateDTO userCreateDTO) {
        String email = userCreateDTO.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        User newUser = new User();
        newUser.setName(userCreateDTO.getName());
        newUser.setEmail(email);
        newUser.setPassword(encryptedPassword);
        newUser.setRole(UserRole.CLIENT);

        return userRepository.save(newUser);
    }
}