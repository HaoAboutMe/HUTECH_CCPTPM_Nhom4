package com.example.webbanmaytinh.controllers;

import com.example.webbanmaytinh.dto.ProductResponse;
import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.service.CategoryService;
import com.example.webbanmaytinh.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        // Get all categories with their products
        List<Category> categories = categoryService.getAllCategories();

        // Get all products
        List<ProductResponse> allProducts = productService.getAllProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();

        // Get discounted products
        List<ProductResponse> discountedProducts = productService.getDiscountedProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();

        model.addAttribute("categories", categories);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("discountedProducts", discountedProducts);
        model.addAttribute("activeMenu", "home");
        return "home";
    }

    @GetMapping("/home")
    public String homeAlias() {
        return "redirect:/";
    }
}

