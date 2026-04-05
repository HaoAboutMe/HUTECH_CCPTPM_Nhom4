package com.example.webbanmaytinh.controllers;

import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.entity.User;
import com.example.webbanmaytinh.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activeMenu", "categories");
        return "categories/list";
    }

    @GetMapping("/new")
    public String createForm(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeMenu", "categories");
        return "categories/form";
    }

    @PostMapping
    public String create(Category category) {
        categoryService.createCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        Category category = categoryService.getCategoryByID(id);
        if (category == null) {
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("isEdit", true);
        model.addAttribute("activeMenu", "categories");
        return "categories/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, Category category) {
        categoryService.updateCategory(id, category);
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
