package com.ingeduardo.demostore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LowStockProductDto {
    private String productId;
    private String productName;
    private int stock;
}
