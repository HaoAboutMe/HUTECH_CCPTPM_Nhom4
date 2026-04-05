package com.example.webbanmaytinh.controllers;

import com.example.webbanmaytinh.dto.CartItem;
import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.entity.Product;
import com.example.webbanmaytinh.service.CartService;
import com.example.webbanmaytinh.service.PcBuildService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pc-build")
@RequiredArgsConstructor
public class PcBuildController {

    private final PcBuildService pcBuildService;
    private final CartService cartService;

    @GetMapping
    public String pcBuildPage(Model model) {
        List<Category> categories = pcBuildService.getPcBuildCategories();
        model.addAttribute("categories", categories);
        return "pc-build";
    }

    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> getProductsByCategory(@RequestParam String categoryId) {
        return pcBuildService.getProductsByCategory(categoryId);
    }

    @PostMapping("/api/add-to-cart")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addBuildToCart(@RequestBody List<CartItem> items, HttpSession session) {
        // Clear current cart items to replace with new build
        cartService.clearCart(session);
        
        for (CartItem item : items) {
            item.setQuantity(1); // Default to 1
            cartService.addToCart(session, item);
        }
        
        return ResponseEntity.ok(Map.of("message", "Build added correctly"));
    }
}
