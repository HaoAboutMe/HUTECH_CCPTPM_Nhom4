package com.example.webbanmaytinh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private String[] ADMINENPOINT = { "/products/**", "/categories/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/register", "/login", "/logout", "/css/**", "/js/**",
                                "/images/**", "/pc-build/**", "/cart/**")
                        .permitAll()

                        // Ngăn chặn tất cả các phương thức (GET, POST, PUT, DELETE) vào admin endpoints
                        // nếu không phải ADMIN
                        .requestMatchers(ADMINENPOINT).hasRole("ADMIN")

                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Nếu cố gắng truy cập mà không có quyền, chuyển hướng về trang chủ với thông
                            // báo lỗi
                            response.sendRedirect("/?error=access_denied");
                        }))
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}
