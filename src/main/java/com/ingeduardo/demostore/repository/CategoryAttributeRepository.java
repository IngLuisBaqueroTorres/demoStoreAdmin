package com.ingeduardo.demostore.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingeduardo.demostore.model.CategoryAttribute;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, UUID> {
    List<CategoryAttribute> findByCategoryId(String categoryId);
}

