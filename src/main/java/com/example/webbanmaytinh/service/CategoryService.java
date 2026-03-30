package com.example.webbanmaytinh.service;

import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByID(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(String id, Category category) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    return categoryRepository.save(existing);
                })
                .orElse(null);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}

