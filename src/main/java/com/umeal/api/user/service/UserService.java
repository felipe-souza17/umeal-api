package com.umeal.api.user.service;

import com.umeal.api.exception.user.EmailAlreadyExistsException;
import com.umeal.api.exception.user.UserNotFoundException;
import com.umeal.api.user.dto.UserCreateDTO;
import com.umeal.api.user.dto.UserResponseDTO;
import com.umeal.api.user.dto.UserUpdateDTO;
import com.umeal.api.user.model.User;
import com.umeal.api.user.model.UserRole;
import com.umeal.api.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());
        return responseDTO;
    }

    public User registerClient(UserCreateDTO userCreateDTO) {
        return registerUser(userCreateDTO, UserRole.CLIENT);
    }

    public User registerOwner(UserCreateDTO userCreateDTO) {
        return registerUser(userCreateDTO, UserRole.RESTAURANT_OWNER);
    }

    @Transactional
    public User registerUser(UserCreateDTO userCreateDTO, UserRole role) {
        String email = userCreateDTO.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        User newUser = new User();
        newUser.setName(userCreateDTO.getName());
        newUser.setEmail(email);
        newUser.setPassword(encryptedPassword);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email)
            );

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            List.of(authority)
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuário", email));
        
        return mapToUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUserProfile(String email, UserUpdateDTO updateDTO) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuário", email));

        user.setName(updateDTO.getName());

        User updatedUser = userRepository.save(user);
        
        return mapToUserResponseDTO(updatedUser);
    }
}