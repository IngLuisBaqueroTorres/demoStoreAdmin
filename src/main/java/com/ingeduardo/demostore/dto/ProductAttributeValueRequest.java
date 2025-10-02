package com.ingeduardo.demostore.dto;

import lombok.Data;

@Data
public class ProductAttributeValueRequest {
    private String attributeName;
    private String value;
}