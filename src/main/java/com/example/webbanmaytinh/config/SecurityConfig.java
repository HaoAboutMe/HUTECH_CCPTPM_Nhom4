package com.example.webbanmaytinh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .anyRequest().permitAll() // Cho phép tất cả mọi trang (Home, Contact, v.v...)
            )
            .csrf(csrf -> csrf.disable()) // Tắt CSRF để dễ test form hơn
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Cho phép map/h2-console nếu có
        
        return http.build();
    }
}
