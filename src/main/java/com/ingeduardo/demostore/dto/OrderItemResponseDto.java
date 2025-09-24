package com.ingeduardo.demostore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
