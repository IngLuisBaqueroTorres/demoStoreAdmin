package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.service.ProductAttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attributes")
public class ProductAttributeController {

    private final ProductAttributeService service;

    public ProductAttributeController(ProductAttributeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductAttribute> create(@RequestBody ProductAttribute attribute) {
        return ResponseEntity.ok(service.create(attribute));
    }

    @GetMapping
    public ResponseEntity<List<ProductAttribute>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttribute> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductAttribute> update(@PathVariable String id, @RequestBody ProductAttribute attribute) {
        return ResponseEntity.ok(service.update(id, attribute));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
