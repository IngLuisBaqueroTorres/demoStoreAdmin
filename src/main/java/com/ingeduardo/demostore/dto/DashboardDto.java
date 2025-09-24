package com.ingeduardo.demostore.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardDto {
    private SalesSummaryDto sales;
    private List<OrderResponseDto> recentOrders;
    private List<LowStockProductDto> lowStockProducts;
}
