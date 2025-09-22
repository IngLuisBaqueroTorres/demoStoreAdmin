package com.ingeduardo.demostore.controller;

import java.util.Arrays;

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
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.service.ProductService;

import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

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
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String userRole = getUserRole(authentication);
        Page<Product> productsPage = productService.getAllProducts(userRole, PageRequest.of(page, size));

        Page<ProductResponseDto> dtoPage = productsPage.map(this::mapToResponseDTO);
        return ResponseEntity.ok(dtoPage);
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
        Product updated = productService.updateProduct(id, productDTO, userRole);
        return updated != null ? ResponseEntity.ok(mapToResponseDTO(updated)) : ResponseEntity.notFound().build();
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
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
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

        Page<Product> resultPage = productService.search(name, category, brand, pageable);
        return ResponseEntity.ok(resultPage);
    }

    private ProductResponseDto mapToResponseDTO(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        Double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;
        Double price = product.getPrice() != null ? product.getPrice() : 0.0;

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(price);
        dto.setStock(product.getStock());
        dto.setActive(product.getActive());
        dto.setDiscount(discount);
        dto.setFinalPrice(product.getFinalPrice());

        if (product.getCategory() != null) {
            Category category = new Category();
            category.setId(product.getCategory().getId());
            category.setName(product.getCategory().getName());
            category.setDescription(product.getCategory().getDescription());
            dto.setCategory(category);
        }

        return dto;
    }

}
