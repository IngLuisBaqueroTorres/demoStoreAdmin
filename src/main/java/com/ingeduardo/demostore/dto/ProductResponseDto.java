package com.ingeduardo.demostore.dto;

import java.util.List;

import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private double finalPrice;
    private int stock;
    private Category category;
    private boolean active;
    private boolean soldOut;
    private Brand brand;
    private List<String> images;
    private List<ProductAttributeResponseDto> attributes;
}
