package com.example.webbanmaytinh.controllers;

import java.util.List;
import java.util.Optional;

import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthenticationController {

    private static final String AUTH_USER_SESSION_KEY = "loggedInUser";

    private final LoginService loginService;

    public AuthenticationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session
    ) {
        Optional<User> loggedInUser = loginService.login(email, password);
        if (loggedInUser.isPresent()) {
            User user = loggedInUser.get();

            // Tạo Authentication cho Spring Security
            List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name())
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            // Quan trọng: Lưu SecurityContext vào session để duy trì đăng nhập
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
            session.setAttribute(AUTH_USER_SESSION_KEY, user);

            return new RedirectView("/?loginSuccess=1");
        }

        return new RedirectView("/login?error=invalid_credentials");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(AUTH_USER_SESSION_KEY);
        SecurityContextHolder.clearContext();
        session.invalidate(); // Xóa toàn bộ session
        return "redirect:/";
    }
}
