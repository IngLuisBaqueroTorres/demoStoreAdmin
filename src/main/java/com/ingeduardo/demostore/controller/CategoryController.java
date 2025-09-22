package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.dto.CategoryResponseDto;
import com.ingeduardo.demostore.mapper.CategoryMapper;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            Pageable pageable) {
        Page<Category> categoriesPage = service.search(name, description, pageable);
        Page<CategoryResponseDto> dtoPage = categoriesPage.map(CategoryMapper::toResponseDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable String id) {
        Category category = service.findById(id);
        return ResponseEntity.ok(CategoryMapper.toResponseDto(category));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryDto) {
        Category newCategory = service.save(categoryDto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCategory.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(CategoryMapper.toResponseDto(newCategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequestDto categoryRequest) {
        Category updatedCategory = service.update(id, categoryRequest);
        return ResponseEntity.ok(CategoryMapper.toResponseDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
