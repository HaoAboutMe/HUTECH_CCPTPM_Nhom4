package com.example.webbanmaytinh.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webbanmaytinh.entity.Role;
import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.entity.enums.RoleName;
import com.example.webbanmaytinh.repository.RoleRepository;
import com.example.webbanmaytinh.repository.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(String email, String password, String confirmPassword, LocalDate dob) {
        if (isBlank(email) || isBlank(password) || isBlank(confirmPassword) || dob == null) {
            throw new IllegalArgumentException("invalid_input");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("password_mismatch");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("email_exists");
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password.trim()));
        user.setDob(dob);
        user.setRole(getOrCreateRole(RoleName.USER));

        userRepository.save(user);
    }

    private Role getOrCreateRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
