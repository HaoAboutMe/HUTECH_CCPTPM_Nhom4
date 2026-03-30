package com.example.webbanmaytinh.service;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.webbanmaytinh.entity.Role;
import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.entity.enums.RoleName;
import com.example.webbanmaytinh.repository.RoleRepository;
import com.example.webbanmaytinh.repository.UserRepository;

@Component
public class StartupDataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "123456";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StartupDataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        getOrCreateRole(RoleName.USER);
        Role adminRole = getOrCreateRole(RoleName.ADMIN);

        User admin = userRepository.findByEmailIgnoreCase(ADMIN_EMAIL)
                .orElseGet(User::new);

        admin.setEmail(ADMIN_EMAIL);
        if (admin.getPassword() == null || !passwordEncoder.matches(ADMIN_PASSWORD, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        }
        if (admin.getDob() == null) {
            admin.setDob(LocalDate.of(2000, 1, 1));
        }
        admin.setRole(adminRole);

        userRepository.save(admin);
    }

    private Role getOrCreateRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
