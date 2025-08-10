package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.controller.CategoryController;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.repository.CategoryRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category save(Category newCategory) {
        logger.warn("Creating category: {}", newCategory.getName());

        if (repository.existsByName(newCategory.getName())) {
            throw new RuntimeException("The category already exists.");
        }
        Category category = new Category(
                newCategory.getName(),
                newCategory.getDescription());
        logger.warn("Saving category: {}", category.getName());
        logger.warn("Saving category: {}", category.getDescription());
        return repository.save(category);
    }

    public Category update(String id, Category updatedCategory) {
        UUID uuid = UUID.fromString(id);
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        if (!existingCategory.getName().equals(updatedCategory.getName())
                && repository.existsByName(updatedCategory.getName())) {
            throw new RuntimeException("Another category with this name already exists.");
        }

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());

        return repository.save(existingCategory);
    }

    public Page<Category> search(String name, String description, Pageable pageable) {
        return repository.search(name, description, pageable);
    }

    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        repository.deleteById(id);
    }

    public Category findById(String id) {
        UUID uuid = UUID.fromString(id);
        return repository.findById(id).orElse(null);
    }
}
