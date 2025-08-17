package com.ingeduardo.demostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingeduardo.demostore.model.CategoryAttributeValue;

@Repository
public interface CategoryAttributeValueRepository extends JpaRepository<CategoryAttributeValue, String> {
    List<CategoryAttributeValue> findByAttributeId(String attributeId);
}