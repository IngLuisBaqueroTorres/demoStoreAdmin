package com.ingeduardo.demostore.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {
    private String id;
    private String name;
    private String description;
    private List<CategoryResponseDto> children;
}
