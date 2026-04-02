package com.example.webbanmaytinh.controllers;

import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.entity.Product;
import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.service.CategoryService;
import com.example.webbanmaytinh.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activeMenu", "products");
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeMenu", "products");
        return "products/form";
    }

    @PostMapping
    public String create(@Valid Product product, BindingResult bindingResult, Model model) {
        Category selected = product.getCategory() != null ? categoryService.getCategoryByID(product.getCategory().getId()) : null;
        product.setCategory(selected);
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeMenu", "products");
            return "products/form";
        }
        try {
            productService.createProduct(product);
            return "redirect:/products";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeMenu", "products");
            model.addAttribute("errorMessage", ex.getMessage());
            return "products/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        Product product = productService.getProductByID(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeMenu", "products");
        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @Valid Product product, BindingResult bindingResult, Model model) {
        Category selected = product.getCategory() != null ? categoryService.getCategoryByID(product.getCategory().getId()) : null;
        product.setCategory(selected);
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "products");
            return "products/form";
        }
        try {
            productService.updateProduct(id, product);
            return "redirect:/products";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "products");
            model.addAttribute("errorMessage", ex.getMessage());
            return "products/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
