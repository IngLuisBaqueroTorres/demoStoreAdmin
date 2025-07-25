package com.ingeduardo.demostore.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.CategoryAttribute;
import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.repository.CategoryAttributeRepository;

@Service
public class CategoryAttributeService {

    @Autowired
    private CategoryAttributeRepository repository;

    public List<CategoryAttribute> getAttributesByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public void assignAttributeToCategory(UUID categoryId, UUID attributeId) {
        CategoryAttribute ca = new CategoryAttribute();
        ca.setId(UUID.randomUUID());
        ca.setCategory(new Category(categoryId));
        ca.setAttribute(new ProductAttribute(attributeId));
        repository.save(ca);
    }
}
