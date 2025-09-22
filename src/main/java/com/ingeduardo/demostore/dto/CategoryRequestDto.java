package com.ingeduardo.demostore.dto;

import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private String description;
    private String parentId;
}
