package com.umeal.api.user.service;

import com.umeal.api.exception.user.EmailAlreadyExistsException;
import com.umeal.api.user.dto.UserCreateDTO;
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
}