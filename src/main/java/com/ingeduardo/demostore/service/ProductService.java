package com.ingeduardo.demostore.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto productRequest);
    ProductResponseDto updateProduct(String id, ProductRequestDto productRequest);
    void deleteProduct(String id);
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    ProductResponseDto getProductById(String id);
    Page<ProductResponseDto> search(String name, String category, String brandName, Pageable pageable);

}
