package com.ingeduardo.demostore.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.model.Product;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto productRequest, String userRole);
    Product updateProduct(String id, ProductRequestDto productRequest, String userRole);
    void deleteProduct(String id, String userRole);
    Page<Product> getAllProducts(String userRole, Pageable pageable);
    ProductResponseDto getProductById(String id, String userRole);
    Page<Product> search(String name, String category, String brand, Pageable pageable);

}
