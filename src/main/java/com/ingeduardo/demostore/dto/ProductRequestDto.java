package com.ingeduardo.demostore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductRequestDto {

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discount = BigDecimal.ZERO;

    // Brand is optional; if not provided the default "Otros" brand will be used
    private String brandId;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    private boolean isActive = true;
    private boolean isSoldOut = false;
    private List<String> images;

    // SKU is optional; backend will generate one if not provided
    private String sku;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    private List<ProductAttributeValueRequest> attributes;
}