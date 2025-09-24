package com.ingeduardo.demostore.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShippingMethodResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
}
