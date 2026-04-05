package com.example.webbanmaytinh.controllers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.webbanmaytinh.service.RegisterService;

@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping({ "/register"})
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public RedirectView submitRegister(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob
    ) {
        try {
            registerService.register(email, password, confirmPassword, dob);
            return new RedirectView("/register?success=1");
        } catch (IllegalArgumentException ex) {
            return new RedirectView("/register?error=" + ex.getMessage());
        }
    }
}
