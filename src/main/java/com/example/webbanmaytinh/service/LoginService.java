package com.example.webbanmaytinh.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> login(String email, String rawPassword) {
        if (isBlank(email) || isBlank(rawPassword)) {
            return Optional.empty();
        }

        String normalizedEmail = email.trim().toLowerCase();
        return userRepository.findByEmailIgnoreCase(normalizedEmail)
                .filter(user -> passwordEncoder.matches(rawPassword.trim(), user.getPassword()));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}