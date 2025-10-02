package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<Page<Brand>> getAllBrands(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        Sort sortOrder = parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        Page<Brand> brandsPage = service.search(name, description, pageRequest);

        return ResponseEntity.ok(brandsPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_PRODUCTS')")
    public ResponseEntity<Brand> getBrandById(@PathVariable String id) {
        Brand brand = service.findById(id);
        return brand != null ? ResponseEntity.ok(brand) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand newBrand = service.save(brand);
        return ResponseEntity.status(201).body(newBrand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<Brand> updateBrand(@PathVariable String id, @RequestBody Brand brandDetails) {
        Brand updatedBrand = service.update(id, brandDetails);
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    public ResponseEntity<Void> deleteBrand(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");
        String property = parts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1) {
            try {
                direction = Sort.Direction.fromString(parts[1]);
            } catch (IllegalArgumentException e) {
                direction = Sort.Direction.ASC;
            }
        }
        return Sort.by(direction, property);
    }
}
