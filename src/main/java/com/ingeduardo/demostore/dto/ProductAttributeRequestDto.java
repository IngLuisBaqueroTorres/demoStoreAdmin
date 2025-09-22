package com.ingeduardo.demostore.dto;

import lombok.Data;

@Data
public class ProductAttributeRequestDto {
    private String name;
    private String type; // STRING, NUMBER, BOOLEAN
}
