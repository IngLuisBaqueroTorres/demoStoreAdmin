package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.ProductAttributeResponseDto;
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
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final String CATEGORY_NOT_FOUND = "Category not found with ID: ";

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, String userRole) {

        authorizeAdmin(userRole);

        String categoryId = dto.getCategory();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND+ categoryId));

        Product product = mapToEntity(dto);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return mapToResponseDto(savedProduct);
    }

    @Override
    public Product updateProduct(String id, ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Double discount = dto.getDiscount() != 0.0 ? dto.getDiscount() : product.getDiscount();
        Double price = dto.getPrice() != 0.0 ? dto.getPrice() : product.getPrice();
        String description = dto.getDescription() != null ? dto.getDescription() : product.getDescription();

        product.setName(dto.getName());
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setBrand(dto.getBrand() != null ? dto.getBrand() : product.getBrand());
        product.setSoldOut(dto.isSoldOut());

        String categoryId = dto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + categoryId));
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
    public ProductResponseDto getProductById(String id, String userRole) {

        Product product = productRepository.findByIdWithAttributes(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return mapToResponseDto(product);
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

        Double discount = dto.getDiscount() != 0.0 ? dto.getDiscount() : 0.0;
        Double price = dto.getPrice() != 0.0 ? dto.getPrice() : 0.0;
        Integer stock = dto.getStock() != 0 ? dto.getStock() : 0;

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscount(discount);
        product.setBrand(dto.getBrand());
        product.setPrice(price);
        product.setStock(stock);
        product.setActive(dto.getActive());
        product.setSoldOut(dto.isSoldOut());

        String categoryId = dto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + categoryId));
        product.setCategory(category);

        return product;
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .finalPrice(product.getFinalPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .active(product.getActive())
                .soldOut(product.getSoldOut())
                .brand(product.getBrand())
                .attributes(
                        product.getAttributes() != null ? product.getAttributes().stream()
                                .map(attrValue -> ProductAttributeResponseDto.builder()
                                        .attributeId(attrValue.getAttributeId())
                                        .value(attrValue.getValue())
                                        .build())
                                        .toList()
                                : new ArrayList<>())
                .build();
    }

}
