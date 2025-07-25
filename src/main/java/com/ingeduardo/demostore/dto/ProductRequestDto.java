package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.Category;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private double price;
    private int stock;
    private Category category;
    private boolean active = true;

    // Getters and setters
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
