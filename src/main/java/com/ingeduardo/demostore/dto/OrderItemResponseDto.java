package com.ingeduardo.demostore.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private String productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;
}
