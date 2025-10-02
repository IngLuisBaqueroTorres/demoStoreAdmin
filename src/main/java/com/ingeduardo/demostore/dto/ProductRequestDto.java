package com.ingeduardo.demostore.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductRequestDto {
    private BigDecimal discount = BigDecimal.ZERO;
    private String brandId;
    private int stock;
    private String categoryId;
    private boolean isActive = true;
    private boolean isSoldOut = false;
    private List<String> images;
    private String name;
    private String description;
    private BigDecimal price;

    private List<ProductAttributeValueRequest> attributes;
}