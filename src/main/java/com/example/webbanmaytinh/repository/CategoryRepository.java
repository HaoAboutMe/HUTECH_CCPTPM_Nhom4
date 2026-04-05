package com.example.webbanmaytinh.repository;

import com.example.webbanmaytinh.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByNameIn(List<String> names);
    Optional<Category> findByNameContainingIgnoreCase(String name);
}

