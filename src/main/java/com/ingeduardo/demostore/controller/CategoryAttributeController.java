package com.ingeduardo.demostore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingeduardo.demostore.dto.AssignAttributeRequest;
import com.ingeduardo.demostore.model.CategoryAttribute;
import com.ingeduardo.demostore.service.CategoryAttributeService;

@RestController
@RequestMapping("/api/category-attributes")
public class CategoryAttributeController {

    @Autowired
    private CategoryAttributeService service;

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<CategoryAttribute>> getAttributesByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(service.getAttributesByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<Void> assignAttribute(@RequestBody AssignAttributeRequest request) {
        service.assignAttributeToCategory(request.getCategoryId(), request.getAttributeId());
        return ResponseEntity.ok().build();
    }
}

