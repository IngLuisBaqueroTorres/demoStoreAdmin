package com.ingeduardo.demostore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
}
