package com.ingeduardo.demostore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingeduardo.demostore.model.ProductAttributeValue;
import com.ingeduardo.demostore.service.ProductAttributeValueService;

@RestController
@RequestMapping("/api/products/{productId}/attributes")
public class ProductAttributeValueController {

    @Autowired
    private ProductAttributeValueService attributeValueService;

    @GetMapping
    public ResponseEntity<List<ProductAttributeValue>> getAttributesByProduct(@PathVariable String productId) {
        List<ProductAttributeValue> attributes = attributeValueService.getAttributesByProduct(productId);
        return ResponseEntity.ok(attributes);
    }

    @PostMapping("/{attributeId}")
    public ResponseEntity<ProductAttributeValue> addOrUpdateAttributeValue(@PathVariable String productId,
                                                                           @PathVariable UUID attributeId,
                                                                           @RequestBody String value) {
        try {
            ProductAttributeValue pav = attributeValueService.addOrUpdateAttributeValue(productId, attributeId, value);
            return ResponseEntity.ok(pav);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{attributeValueId}")
    public ResponseEntity<String> deleteAttributeValue(@PathVariable String attributeValueId) {
        try {
            attributeValueService.deleteAttributeValue(attributeValueId);
            return ResponseEntity.ok("Attribute value deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Failed to delete attribute value");
        }
    }
}
