package com.ingeduardo.demostore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAttributeResponseDto {
    private String name;
    private String value;
    private String attributeId;

     public ProductAttributeResponseDto(String attributeId, String value, String name) {
        this.attributeId = attributeId;
        this.value = value;
        this.attributeId = attributeId;
     }
}
