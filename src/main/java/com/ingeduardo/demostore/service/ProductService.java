package com.ingeduardo.demostore.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.model.Product;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto productRequest, String userRole);
    ProductResponseDto updateProduct(String id, ProductRequestDto productRequest, String userRole);
    void deleteProduct(String id, String userRole);
    Page<ProductResponseDto> getAllProducts(String userRole, Pageable pageable);
    ProductResponseDto getProductById(String id, String userRole);
    Page<ProductResponseDto> search(String name, String category, String brandName, Pageable pageable);

}
