package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.dto.CategoryResponseDto;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getTopLevelCategories() {
        List<Category> topLevelCategories = service.findTopLevelCategories();
        List<CategoryResponseDto> dtoList = topLevelCategories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        Sort sortOrder = parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        Page<Category> categoriesPage = service.search(name, description, pageRequest);
        List<CategoryResponseDto> dtoList = categoriesPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageImpl<>(dtoList, pageRequest, categoriesPage.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable String id) {
        Category category = service.findById(id);
        return category != null ? ResponseEntity.ok(toDto(category)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoryDto) {
        Category newCategory = service.save(categoryDto);
        return ResponseEntity.status(201).body(toDto(newCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable String id,
            @RequestBody CategoryRequestDto categoryDto) {

        Category updatedCategory = service.update(id, categoryDto);
        return ResponseEntity.ok(toDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
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

    private CategoryResponseDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(category.getChildren().stream().map(this::toDto).collect(Collectors.toList()));
        }
        return dto;
    }
}
