package com.ingeduardo.demostore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingeduardo.demostore.model.CategoryAttribute;
import com.ingeduardo.demostore.service.CategoryAttributeService;

@RestController
@RequestMapping("/api/categories/{categoryId}/attributes")
public class CategoryAttributeController {

    @Autowired
    private CategoryAttributeService categoryAttributeService;

    @GetMapping
    public ResponseEntity<List<CategoryAttribute>> getAttributesByCategory(@PathVariable String categoryId) {
        List<CategoryAttribute> attributes = categoryAttributeService.getAttributesByCategory(categoryId);
        return ResponseEntity.ok(attributes);
    }

    @PostMapping
    public ResponseEntity<String> assignAttributeToCategory(
            @PathVariable String categoryId,
            @RequestBody AttributeAssignmentRequest request) {
        try {
            categoryAttributeService.assignAttributeToCategory(categoryId, request.getAttributeId());
            return ResponseEntity.ok("Attribute assigned to category successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/relation/{relationId}")
    public ResponseEntity<String> deleteCategoryAttribute(@PathVariable UUID relationId) {
        try {
            categoryAttributeService.deleteCategoryAttribute(relationId);
            return ResponseEntity.ok("Category attribute assignment deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Clase interna para recibir el body
    public static class AttributeAssignmentRequest {
        private UUID attributeId;

        public UUID getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(UUID attributeId) {
            this.attributeId = attributeId;
        }
    }
}
