package com.ingeduardo.demostore.dto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAttributeResponseDto {
    private String id;
    private String name;
    private String type;
    private String description;
    private String categoryId;
}
