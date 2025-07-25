package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);

    List<Category> findByNameContainingIgnoreCase(String name);

    List<Category> findByDescriptionContainingIgnoreCase(String description);

    List<Category> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description);

}
