package com.ingeduardo.demostore.dto;

import java.util.UUID;

public class ProductAttributeValueRequestDto {
    private String productId;
    private String attributeId;
    private String value;

    // Getters y setters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
