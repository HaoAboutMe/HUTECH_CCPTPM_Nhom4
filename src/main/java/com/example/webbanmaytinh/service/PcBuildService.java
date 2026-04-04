package com.example.webbanmaytinh.service;

import com.example.webbanmaytinh.entity.Category;
import com.example.webbanmaytinh.entity.Product;
import com.example.webbanmaytinh.repository.CategoryRepository;
import com.example.webbanmaytinh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PcBuildService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getPcBuildCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        
        // Target categories list for exact or near matches
        List<String> targetNames = Arrays.asList("CPU", "MAINBOARD", "RAM", "VGA", "STORAGE", "PSU", "CASE");

        // Extract categories that match our targets (case-insensitive, trimmed)
        List<Category> filtered = allCategories.stream()
                .filter(cat -> {
                    if (cat.getName() == null) return false;
                    String name = cat.getName().trim().toUpperCase();
                    return targetNames.stream().anyMatch(target -> name.contains(target));
                })
                .collect(Collectors.toList());

        // Sort them according to the sequence in targetNames
        filtered.sort((c1, c2) -> {
            String n1 = c1.getName().trim().toUpperCase();
            String n2 = c2.getName().trim().toUpperCase();
            
            int idx1 = getMatchIndex(n1, targetNames);
            int idx2 = getMatchIndex(n2, targetNames);
            
            return Integer.compare(idx1, idx2);
        });

        return filtered;
    }

    private int getMatchIndex(String name, List<String> targets) {
        for (int i = 0; i < targets.size(); i++) {
            if (name.contains(targets.get(i))) return i;
        }
        return 999;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
