package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.repository.ProductRepository;
import com.ingeduardo.demostore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);
        Product product = mapToEntity(dto);
        Product saved = productRepository.save(product);
        return productRepository.save(saved);
    }

    @Override
    public Product updateProduct(String id, ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
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
    public List<Product> getAllProducts(String userRole) {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id, String userRole) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
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
        product.setCategory(dto.getCategory());
        product.setStock(dto.getStock());
        product.setActive(dto.getActive());
        return product;
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setStock(product.getStock());
        dto.setActive(product.getActive());
        return dto;
    }
}
