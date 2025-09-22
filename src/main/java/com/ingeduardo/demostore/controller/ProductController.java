package com.ingeduardo.demostore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto productDTO,
            Authentication authentication) {
        String userRole = getUserRole(authentication);

        ProductResponseDto created = productService.createProduct(productDTO, userRole);

        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String userRole = getUserRole(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDto> productsPage = productService.getAllProducts(userRole, pageable);

        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String id, Authentication authentication) {
        String userRole = getUserRole(authentication);
        ProductResponseDto productDto = productService.getProductById(id, userRole);
        return productDto != null ? ResponseEntity.ok(productDto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProductResponseDto> update(@PathVariable String id, @RequestBody ProductRequestDto productDTO,
            Authentication authentication) {
        String userRole = getUserRole(authentication);
        ProductResponseDto updated = productService.updateProduct(id, productDTO, userRole);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        String userRole = getUserRole(authentication);
        productService.deleteProduct(id, userRole);
        return ResponseEntity.noContent().build();
    }

    private String getUserRole(Authentication authentication) {
        return authentication != null ? authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .findFirst().orElse("USER") : "USER";
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brandName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        String sortField = "id";

        if (sortParams.length == 2) {
            sortField = sortParams[0];
            direction = Sort.Direction.fromString(sortParams[1]);
        } else if (sortParams.length == 1) {
            sortField = sortParams[0];
        }

        Sort sortOrder = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<ProductResponseDto> resultPage = productService.search(name, category, brandName, pageable);
        return ResponseEntity.ok(resultPage);
    }
}
