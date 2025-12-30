package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.dto.CategoryResponseDto;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<List<CategoryResponseDto>> getTopLevelCategories() {
        return ResponseEntity.ok(service.getTopLevelCategoriesAsDto());
    }

    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<Page<CategoryResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        Sort sortOrder = parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);
        return ResponseEntity.ok(service.searchAsDto(name, description, pageRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable String id) {
        Category category = service.findById(id);
        return category != null ? ResponseEntity.ok(service.toDto(category)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoryDto) {
        Category newCategory = service.save(categoryDto);
        return ResponseEntity.status(201).body(service.toDto(newCategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable String id,
            @RequestBody CategoryRequestDto categoryDto) {

        Category updatedCategory = service.update(id, categoryDto);
        return ResponseEntity.ok(service.toDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");
        String property = parts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1) {
            try {
                direction = Sort.Direction.fromString(parts[1]);
            } catch (IllegalArgumentException e) {
                direction = Sort.Direction.ASC;
            }
        }
        return Sort.by(direction, property);
    }
}
