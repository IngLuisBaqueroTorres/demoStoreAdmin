package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.service.CategoryService;

import org.springframework.http.ResponseEntity;
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
    public List<Category> getAll() {
        return service.findAll();
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
    public List<Category> search(@RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {
        return service.search(name, description);
    }

}
