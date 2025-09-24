package com.ingeduardo.demostore.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShippingMethodRequestDto {
    private String name;
    private String description;
    private BigDecimal cost;
}
