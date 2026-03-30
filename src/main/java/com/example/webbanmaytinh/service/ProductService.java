package com.example.webbanmaytinh.service;

import com.example.webbanmaytinh.entity.Product;
import com.example.webbanmaytinh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        applyDiscountRules(product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getDiscountedProducts() {
        return productRepository.findByIsDiscountedTrueOrderByDiscountPercentDesc();
    }

    public Product getProductByID(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(String id, Product product) {
        applyDiscountRules(product);
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setIsDiscounted(product.getIsDiscounted());
                    existing.setDiscountPercent(product.getDiscountPercent());
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

    /**
     * Business rules:
     * - isDiscounted = false  => discountPercent must be 0
     * - isDiscounted = true   => discountPercent must be > 0
     */
    private void applyDiscountRules(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (product.getPrice() != null && product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }

        Boolean discounted = product.getIsDiscounted() != null && product.getIsDiscounted();
        Integer percent = product.getDiscountPercent() == null ? 0 : product.getDiscountPercent();

        if (!discounted) {
            product.setIsDiscounted(false);
            product.setDiscountPercent(0);
            return;
        }

        product.setIsDiscounted(true);
        product.setDiscountPercent(percent);
        if (percent <= 0) {
            throw new IllegalArgumentException("discountPercent must be > 0 when isDiscounted = true");
        }
        if (percent > 100) {
            throw new IllegalArgumentException("discountPercent must be <= 100");
        }
    }

    public static BigDecimal calculateFinalPrice(BigDecimal price, boolean isDiscounted, int discountPercent) {
        if (price == null) {
            return null;
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        if (!isDiscounted || discountPercent <= 0) {
            return price;
        }
        if (discountPercent > 100) {
            throw new IllegalArgumentException("discountPercent must be <= 100");
        }
        BigDecimal multiplier = BigDecimal.valueOf(100L - discountPercent).divide(BigDecimal.valueOf(100L), 4, RoundingMode.HALF_UP);
        return price.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
