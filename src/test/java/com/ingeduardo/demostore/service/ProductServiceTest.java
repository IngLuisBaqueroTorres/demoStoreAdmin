package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.ProductRequestDto;
import com.ingeduardo.demostore.dto.ProductResponseDto;
import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.repository.BrandRepository;
import com.ingeduardo.demostore.repository.CategoryRepository;
import com.ingeduardo.demostore.repository.ProductRepository;
import com.ingeduardo.demostore.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDto productRequest;
    private Category category;
    private Brand brand;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId("cat1");
        category.setName("Electronics");

        brand = new Brand();
        brand.setId("brand1");
        brand.setName("Sony");

        productRequest = new ProductRequestDto();
        productRequest.setSku("PS5-SKU");
        productRequest.setName("PS5");
        productRequest.setDescription("Console");
        productRequest.setPrice(new BigDecimal("499.99"));
        productRequest.setStock(10);
        productRequest.setCategoryId("cat1");
        productRequest.setBrandId("brand1");

        product = new Product();
        product.setId("prod1");
        product.setSku("PS5-SKU");
        product.setName("PS5");
        product.setCategory(category);
        product.setBrand(brand);
        product.setPrice(new BigDecimal("499.99"));
        product.setStock(10);
        product.setIsActive(true);
        product.setSoldOut(false);
        product.setAttributes(new java.util.ArrayList<>());
        product.setImages(new java.util.ArrayList<>());
    }

    @Test
    void createProduct_Success() {
        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(brandRepository.findById("brand1")).thenReturn(Optional.of(brand));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals("PS5", result.getName());
        assertEquals("PS5-SKU", result.getSku());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_withoutBrand_usesDefaultBrand() {
        // simulate client not providing brandId
        productRequest.setBrandId(null);
        Brand defaultBrand = new Brand();
        defaultBrand.setId("default-id");
        defaultBrand.setName("Otros");

        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(brandRepository.findByNameIgnoreCase("Otros")).thenReturn(Optional.of(defaultBrand));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setId("generated-id");
            return saved;
        });

        ProductResponseDto result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertNotNull(result.getBrand());
        assertEquals("Otros", result.getBrand().getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_generatesSkuWhenMissing() {
        productRequest.setSku(null); // simulate client not sending SKU
        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(brandRepository.findById("brand1")).thenReturn(Optional.of(brand));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            // mimic DB assigned id
            saved.setId("generated-id");
            return saved;
        });

        ProductResponseDto result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertNotNull(result.getSku());
        assertFalse(result.getSku().isBlank());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findByIdWithAttributes("prod1")).thenReturn(Optional.of(product));

        ProductResponseDto result = productService.getProductById("prod1");

        assertNotNull(result);
        assertEquals("prod1", result.getId());
    }

    @Test
    void getAllProducts_includesSku() {
        // prepare a page with a product that has SKU
        java.util.List<Product> list = java.util.Collections.singletonList(product);
        org.springframework.data.domain.Page<Product> page = new org.springframework.data.domain.PageImpl<>(list);
        when(productRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        org.springframework.data.domain.Page<ProductResponseDto> result = productService.getAllProducts(org.springframework.data.domain.PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("PS5-SKU", result.getContent().get(0).getSku());
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findByIdWithAttributes("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById("unknown"));
    }
}
