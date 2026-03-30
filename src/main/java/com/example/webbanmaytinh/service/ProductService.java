package com.example.webbanmaytinh.service;

import com.example.webbanmaytinh.entity.Product;
import com.example.webbanmaytinh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByID(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(String id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setDescription(product.getDescription());
                    existing.setImageUrl(product.getImageUrl());
                    existing.setQuantity(product.getQuantity());
                    existing.setCategory(product.getCategory());
                    return productRepository.save(existing);
                })
                .orElse(null);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
