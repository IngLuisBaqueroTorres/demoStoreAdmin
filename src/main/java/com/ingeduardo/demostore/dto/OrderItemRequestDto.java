package com.ingeduardo.demostore.dto;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private String productId;
    private Integer quantity;
}
