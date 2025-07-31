package com.ingeduardo.demostore.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ingeduardo.demostore.dto.CategoryIdDto;
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
    public ResponseEntity<List<ProductResponseDto>> getAll(Authentication authentication) {
        String userRole = getUserRole(authentication);
        List<Product> products = productService.getAllProducts(userRole);
        List<ProductResponseDto> dtoList = products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String id, Authentication authentication) {
        String userRole = getUserRole(authentication);
        Product product = productService.getProductById(id, userRole);
        return product != null ? ResponseEntity.ok(mapToResponseDTO(product)) : ResponseEntity.notFound().build();
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
    public List<Product> search(@RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category) {
        return productService.search(name, description, category);
    }

    private ProductResponseDto mapToResponseDTO(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setActive(product.getActive());

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
