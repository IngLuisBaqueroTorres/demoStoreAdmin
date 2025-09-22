package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.*;
import com.ingeduardo.demostore.model.CategoryAttributeValue;
import com.ingeduardo.demostore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/category-attributes")
@RequiredArgsConstructor
public class CategoryAttributeController {

    private final CategoryAttributeService attributeService;
    private final CategoryAttributeValueService valueService;

    @PostMapping
    public ResponseEntity<CategoryAttributeResponseDto> create(@RequestBody CategoryAttributeRequestDto dto) {
        var created = attributeService.createAttribute(dto);
        return ResponseEntity.created(URI.create("/api/category-attributes/" + created.getId())).body(created);
    }

    @GetMapping("/category/{categoryId}")
    public List<CategoryAttributeResponseDto> getByCategory(@PathVariable String categoryId) {
        return attributeService.getAttributesByCategory(categoryId);
    }

    @PutMapping("/{id}")
    public CategoryAttributeResponseDto update(@PathVariable String id, @RequestBody CategoryAttributeRequestDto dto) {
        return attributeService.updateAttribute(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }

    // Values
    @PostMapping("/values")
    public ResponseEntity<CategoryAttributeValue> addValue(@RequestBody CategoryAttributeValueRequestDto dto) {
        var created = valueService.addValueToAttribute(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{attributeId}/values")
    public List<CategoryAttributeValue> getValues(@PathVariable String attributeId) {
        return valueService.getValuesByAttribute(attributeId);
    }
}
