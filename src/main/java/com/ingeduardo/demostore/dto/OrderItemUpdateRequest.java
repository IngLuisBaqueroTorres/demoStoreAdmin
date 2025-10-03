package com.ingeduardo.demostore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderItemUpdateRequest {

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}