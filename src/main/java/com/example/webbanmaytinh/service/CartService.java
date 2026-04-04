package com.example.webbanmaytinh.service;

import com.example.webbanmaytinh.dto.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, CartItem item) {
        List<CartItem> cart = getCart(session);
        
        // Check if item already exists
        for (CartItem existingItem : cart) {
            if (existingItem.getProductId().equals(item.getProductId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        
        cart.add(item);
    }

    public void clearCart(HttpSession session) {
        session.setAttribute(CART_SESSION_KEY, new ArrayList<CartItem>());
    }

    public long calculateTotal(HttpSession session) {
        return getCart(session).stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
