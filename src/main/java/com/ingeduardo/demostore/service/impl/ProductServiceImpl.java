package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.ProductAttributeResponseDto;
import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.repository.BrandRepository;
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
    private final BrandRepository brandRepository;
    private static final String CATEGORY_NOT_FOUND = "Category not found with ID: ";
    private static final String BRAND_NOT_FOUND = "Brand not found with ID: ";

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);
        Product product = mapToEntity(dto);
        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(String id, ProductRequestDto dto, String userRole) {
        authorizeAdmin(userRole);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setStock(dto.getStock());
        product.setIsActive(dto.getActive());
        product.setSoldOut(dto.isSoldOut());

        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + dto.getCategory()));
            product.setCategory(category);
        }

        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND + dto.getBrandId()));
            product.setBrand(brand);
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
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
    public Page<ProductResponseDto> getAllProducts(String userRole, Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(this::mapToResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(String id, String userRole) {

        Product product = productRepository.findByIdWithAttributes(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return mapToResponseDto(product);
    }

    @Override
    public Page<ProductResponseDto> search(String name, String category, String brandName, Pageable pageable) {
        Page<Product> productsPage = productRepository.search(name, category, brandName, pageable);
        return productsPage.map(this::mapToResponseDto);
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
        product.setDiscount(dto.getDiscount());
        product.setIsActive(dto.getActive());
        product.setSoldOut(dto.isSoldOut());
        product.setImages(dto.getImages());

        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + dto.getCategory()));
            product.setCategory(category);
        }

        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND + dto.getBrandId()));
            product.setBrand(brand);
        }

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
                .active(product.getIsActive())
                .soldOut(product.getIsSoldOut())
                .brand(product.getBrand())
                .images(product.getImages())
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
