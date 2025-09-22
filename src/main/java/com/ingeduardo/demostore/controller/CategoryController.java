package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Category>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        Sort sortOrder = parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        Page<Category> categoriesPage = service.search(name, description, pageRequest);

        return ResponseEntity.ok(categoriesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable String id) {
        Category category = service.findById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @RequestBody Category categoryRequest) {

        Category updatedCategory = service.update(id, categoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Category>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        Sort sortOrder = parseSort(sort);

        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        Page<Category> result = service.search(name, description, pageRequest);

        return ResponseEntity.ok(result);
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
