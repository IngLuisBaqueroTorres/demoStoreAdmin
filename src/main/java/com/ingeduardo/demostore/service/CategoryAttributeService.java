package com.ingeduardo.demostore.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.CategoryAttribute;
import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.repository.CategoryAttributeRepository;
import com.ingeduardo.demostore.repository.CategoryRepository;
import com.ingeduardo.demostore.repository.ProductAttributeRepository;

@Service
public class CategoryAttributeService {

    @Autowired
    private CategoryAttributeRepository categoryAttributeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    public List<CategoryAttribute> getAttributesByCategory(String categoryId) {
        return categoryAttributeRepository.findByCategoryId(categoryId);
    }

    public CategoryAttribute createCategoryAttribute(CategoryAttribute categoryAttribute) {
        categoryRepository.findById(categoryAttribute.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return categoryAttributeRepository.save(categoryAttribute);
    }

    public void assignAttributeToCategory(String categoryId, UUID attributeId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        ProductAttribute attribute = productAttributeRepository.findById(attributeId.toString())
                .orElseThrow(() -> new RuntimeException("Attribute not found: " + attributeId));

        boolean exists = categoryAttributeRepository
                .findByCategoryId(categoryId)
                .stream()
                .anyMatch(ca -> ca.getAttribute().getId().equals(attributeId.toString()));

        if (exists) {
            throw new RuntimeException("Attribute already assigned to this category");
        }

        CategoryAttribute ca = new CategoryAttribute();
        ca.setId(UUID.randomUUID());
        ca.setCategory(category);
        ca.setAttribute(attribute);

        categoryAttributeRepository.save(ca);
    }

    public void deleteCategoryAttribute(UUID relationId) {
        CategoryAttribute ca = categoryAttributeRepository.findById(relationId)
                .orElseThrow(() -> new RuntimeException("CategoryAttribute relation not found: " + relationId));
        categoryAttributeRepository.delete(ca);
    }
    

}
