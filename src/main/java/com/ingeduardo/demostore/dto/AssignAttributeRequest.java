package com.ingeduardo.demostore.dto;

import java.util.UUID;

public class AssignAttributeRequest {
    private String categoryId;
    private UUID attributeId;

    // Getters y setters
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public UUID getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(UUID attributeId) {
        this.attributeId = attributeId;
    }
}

