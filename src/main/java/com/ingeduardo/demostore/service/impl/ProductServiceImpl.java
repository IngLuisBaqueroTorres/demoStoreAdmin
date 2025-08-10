package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.controller.CategoryController;
import com.ingeduardo.demostore.dto.CategoryIdDto;
import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.repository.CategoryRepository;
import com.ingeduardo.demostore.repository.ProductRepository;
import com.ingeduardo.demostore.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);
        logger.warn("userRole getName: {}", dto.getName());
        logger.warn("userRole getCategory: {}", dto.getCategory());
        logger.warn("userRole getActive: {}", dto.getActive());
        String categoryId = dto.getCategory().toString();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        Product product = mapToEntity(dto);
        logger.warn("categorycategorycategory getName: {}", category.getName());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return mapToResponseDto(savedProduct);
    }

    @Override
    public Product updateProduct(String id, ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        String categoryId = dto.getCategory().toString();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        product.setCategory(category);

        product.setStock(dto.getStock());
        product.setActive(dto.getActive());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id, String userRole) {
        authorizeAdmin(userRole);

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getAllProducts(String userRole, Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(String id, String userRole) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Page<Product> search(String name, String category, String brand, Pageable pageable) {
        return productRepository.search(name, category, brand, pageable);
    }

    private void authorizeAdmin(String userRole) {
        if (!userRole.equals("ROLE_ADMIN") && !userRole.equals("ROLE_SUPER_ADMIN")) {
            throw new SecurityException("You are not authorized to perform this action.");
        }
    }

    private Product mapToEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setActive(dto.getActive());

        String categoryId = dto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        product.setCategory(category);

        return product;
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setActive(product.getActive());

        CategoryIdDto categoryDto = new CategoryIdDto();
        categoryDto.setId(product.getCategory().getId());
        dto.setCategory(product.getCategory());
        return dto;
    }
}
