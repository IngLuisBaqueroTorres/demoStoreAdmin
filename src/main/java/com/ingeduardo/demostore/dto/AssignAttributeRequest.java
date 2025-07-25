package com.ingeduardo.demostore.dto;

import java.util.UUID;

public class AssignAttributeRequest {
    private UUID categoryId;
    private UUID attributeId;

    // Getters y setters
    public UUID getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
    public UUID getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(UUID attributeId) {
        this.attributeId = attributeId;
    }
}

