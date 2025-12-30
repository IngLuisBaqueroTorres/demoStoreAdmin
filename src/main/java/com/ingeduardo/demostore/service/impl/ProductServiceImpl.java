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
    private static final String DEFAULT_BRAND_NAME = "Otros"; // default brand when not provided

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {

        Product product = mapToEntity(dto);
        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(String id, ProductRequestDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        if (dto.getSku() != null) {
            product.setSku(dto.getSku());
        }
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setStock(dto.getStock());
        product.setIsActive(dto.isActive());
        product.setSoldOut(dto.isSoldOut());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + dto.getCategoryId()));
            product.setCategory(category);
        }

        if (dto.getBrandId() != null) {
            if (dto.getBrandId().trim().isEmpty()) {
                product.setBrand(getOrCreateDefaultBrand());
            } else {
                Brand brand = brandRepository.findById(dto.getBrandId())
                        .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND + dto.getBrandId()));
                product.setBrand(brand);
            }
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(String id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(this::mapToResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(String id) {

        Product product = productRepository.findByIdWithAttributes(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return mapToResponseDto(product);
    }

    @Override
    public Page<ProductResponseDto> search(String name, String category, String brandName, Pageable pageable) {
        Page<Product> productsPage = productRepository.search(name, category, brandName, pageable);
        return productsPage.map(this::mapToResponseDto);
    }

    private Product mapToEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        // If SKU provided use it, otherwise generate one
        if (dto.getSku() != null && !dto.getSku().trim().isEmpty()) {
            product.setSku(dto.getSku());
        } else {
            product.setSku(generateSku(dto.getName()));
        }
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscount(dto.getDiscount());
        product.setIsActive(dto.isActive());
        product.setSoldOut(dto.isSoldOut());
        product.setImages(dto.getImages());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND + dto.getCategoryId()));
            product.setCategory(category);
        }

        // If brandId provided and not blank use it; otherwise assign default brand
        if (dto.getBrandId() != null && !dto.getBrandId().trim().isEmpty()) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND + dto.getBrandId()));
            product.setBrand(brand);
        } else {
            product.setBrand(getOrCreateDefaultBrand());
        }

        return product;
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku() != null ? product.getSku() : "")
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .finalPrice(product.getFinalPrice())
                .stock(product.getStock() != null ? product.getStock() : 0)
                .category(product.getCategory())
                .active(Boolean.TRUE.equals(product.getIsActive()))
                .soldOut(Boolean.TRUE.equals(product.getIsSoldOut()))
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

    private String generateSku(String name) {
        String base = (name == null || name.trim().isEmpty()) ? "SKU" : name.replaceAll("[^A-Za-z0-9]+", "-").toUpperCase();
        if (base.length() > 20) base = base.substring(0, 20);
        String suffix = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return base + "-" + suffix;
    }

    private Brand getOrCreateDefaultBrand() {
        return brandRepository.findByNameIgnoreCase(DEFAULT_BRAND_NAME)
                .orElseGet(() -> {
                    Brand b = new Brand();
                    b.setName(DEFAULT_BRAND_NAME);
                    b.setDescription("Default brand for uncategorized products");
                    return brandRepository.save(b);
                });
    }

}
