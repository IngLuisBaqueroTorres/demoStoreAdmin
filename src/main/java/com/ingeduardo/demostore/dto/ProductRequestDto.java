package com.ingeduardo.demostore.dto;


import java.util.List;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private double price;
    private double discount = 0.0;
    private String brand;
    private int stock;
    private String  category;
    private boolean active = true;
    private boolean soldOut = false;
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
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
