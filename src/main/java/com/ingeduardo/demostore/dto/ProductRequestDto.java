package com.ingeduardo.demostore.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount = BigDecimal.ZERO;
    private String brandId;
    private int stock;
    private String  category;
    private boolean isActive = true;
    private boolean isSoldOut = false;
    private List<String> images;
    private List<ProductAttributeRequestDto> attributes;

    // Getters and setters
    public String  getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public boolean getActive() {
        return isActive;
    }
    public void isActive(boolean isActive) {
        this.isActive = isActive;
    }
}
