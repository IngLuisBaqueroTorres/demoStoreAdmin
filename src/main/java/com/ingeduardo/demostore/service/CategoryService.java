package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingeduardo.demostore.dto.CategoryResponseDto;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryResponseDto> getTopLevelCategoriesAsDto() {
        return repository.findByParentIsNull().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Page<CategoryResponseDto> searchAsDto(String name, String description, Pageable pageable) {
        Page<Category> categoriesPage = repository.search(name, description, pageable);
        List<CategoryResponseDto> dtoList = categoriesPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, categoriesPage.getTotalElements());
    }

    public Category findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Category save(CategoryRequestDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new RuntimeException("The category already exists.");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        if (dto.getParentId() != null && !dto.getParentId().isBlank()) {
            Category parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found."));
            category.setParent(parent);
        }

        return repository.save(category);
    }

    public Category update(String id, CategoryRequestDto dto) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        if (!existingCategory.getName().equals(dto.getName())
                && repository.existsByName(dto.getName())) {
            throw new RuntimeException("Another category with this name already exists.");
        }

        existingCategory.setName(dto.getName());
        existingCategory.setDescription(dto.getDescription());

        if (dto.getParentId() != null && !dto.getParentId().isBlank()) {
            if (dto.getParentId().equals(existingCategory.getId())) {
                throw new RuntimeException("A category cannot be its own parent.");
            }
            Category parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found."));
            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null);
        }

        return repository.save(existingCategory);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Category not found.");
        }
        repository.deleteById(id);
    }

    public com.ingeduardo.demostore.dto.CategoryResponseDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        com.ingeduardo.demostore.dto.CategoryResponseDto dto = new com.ingeduardo.demostore.dto.CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(category.getChildren().stream()
                    .map(this::toDto)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }
}
