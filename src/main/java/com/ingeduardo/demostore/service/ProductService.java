package com.ingeduardo.demostore.service;

import java.util.List;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.model.Product;

public interface ProductService {
    Product createProduct(ProductRequestDto productRequest, String userRole);
    Product updateProduct(String id, ProductRequestDto productRequest, String userRole);
    void deleteProduct(String id, String userRole);
    List<Product> getAllProducts(String userRole);
    Product getProductById(String id, String userRole);
    List<Product> search(String name, String description, String category);

}
