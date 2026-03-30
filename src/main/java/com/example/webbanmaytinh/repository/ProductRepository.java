package com.example.webbanmaytinh.repository;

import com.example.webbanmaytinh.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIsDiscountedTrueOrderByDiscountPercentDesc();
}

