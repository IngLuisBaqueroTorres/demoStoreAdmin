package com.ingeduardo.demostore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.service.ProductService;

import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<ProductResponseDto> create(
            @jakarta.validation.Valid @RequestBody ProductRequestDto productDTO) {
        ProductResponseDto created = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDto> productsPage = productService.getAllProducts(pageable);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String id) {
        ProductResponseDto productDto = productService.getProductById(id);
        return productDto != null ? ResponseEntity.ok(productDto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<ProductResponseDto> update(@PathVariable String id,
            @jakarta.validation.Valid @RequestBody ProductRequestDto productDTO) {
        ProductResponseDto updated = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
