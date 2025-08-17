package com.ingeduardo.demostore.controller;


import org.springframework.web.bind.annotation.*;

import com.ingeduardo.demostore.dto.ProductAttributeValueResponseDto;
import com.ingeduardo.demostore.dto.ProductAttributeValueRequestDto;
import com.ingeduardo.demostore.service.ProductAttributeValueService;

@RestController
@RequestMapping("/api/product-attribute-values")
public class ProductAttributeValueController {

    private final ProductAttributeValueService service;

    public ProductAttributeValueController(ProductAttributeValueService service) {
        this.service = service;
    }

    @PostMapping
    public ProductAttributeValueResponseDto create(@RequestBody  ProductAttributeValueRequestDto request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ProductAttributeValueResponseDto update(@PathVariable String id, @RequestBody ProductAttributeValueRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/product/{productId}")
    public java.util.List<ProductAttributeValueResponseDto> findByProductId(@PathVariable String productId) {
        return service.findByProductId(productId);
    }
    
}
