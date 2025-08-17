package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.CategoryAttribute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, String> {
    List<CategoryAttribute> findByCategoryId(String categoryId);
}
