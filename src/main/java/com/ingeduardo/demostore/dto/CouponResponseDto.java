package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponResponseDto {
    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate expiryDate;
    private Integer usageLimit;
    private Integer timesUsed;
    private BigDecimal minPurchaseAmount;
    private Boolean isActive;
}
