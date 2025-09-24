package com.ingeduardo.demostore.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SalesSummaryDto {
    private BigDecimal today;
    private BigDecimal thisWeek;
    private BigDecimal thisMonth;
}
