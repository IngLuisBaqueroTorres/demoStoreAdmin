package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.Category;

import lombok.Data;

@Data
public class ProductResponseDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private double finalPrice;
    private int stock;
    private Category category;
    private boolean active = true;
    private boolean soldOut = false;
    private String brand;
}
