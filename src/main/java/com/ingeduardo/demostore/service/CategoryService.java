package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.mapper.CategoryMapper;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category save(CategoryRequestDto categoryDto) {
        if (repository.existsByName(categoryDto.getName())) {
            throw new IllegalStateException("A category with this name already exists.");
        }
        Category category = CategoryMapper.toEntity(categoryDto);
        return repository.save(category);
    }

    public Category update(String id, CategoryRequestDto categoryDto) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!existingCategory.getName().equals(categoryDto.getName())
                && repository.existsByName(categoryDto.getName())) {
            throw new IllegalStateException("Another category with this name already exists.");
        }

        CategoryMapper.updateEntityFromDto(categoryDto, existingCategory);
        return repository.save(existingCategory);
    }

    public Page<Category> search(String name, String description, Pageable pageable) {
        return repository.search(name, description, pageable);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public Category findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
