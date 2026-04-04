package com.example.webbanmaytinh.controllers;

import com.example.webbanmaytinh.dto.CartItem;
import com.example.webbanmaytinh.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cartItems = cartService.getCart(session);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", cartService.calculateTotal(session));
        return "cart";
    }
}
