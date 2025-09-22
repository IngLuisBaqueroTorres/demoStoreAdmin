package com.ingeduardo.demostore.dto;


public class AssignAttributeRequest {
    private String categoryId;
    private String attributeId;

    // Getters y setters
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
}

