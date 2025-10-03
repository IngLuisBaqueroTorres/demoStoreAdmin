package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.DashboardDto;
import com.ingeduardo.demostore.dto.LowStockProductDto;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.dto.SalesSummaryDto;
import com.ingeduardo.demostore.model.Order;
import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.repository.OrderRepository;
import com.ingeduardo.demostore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService; // To reuse the mapping logic

    private static final int LOW_STOCK_THRESHOLD = 5;

    public DashboardDto getDashboardData() {
        SalesSummaryDto salesSummary = getSalesSummary();
        List<OrderResponseDto> recentOrders = getRecentOrders();
        List<LowStockProductDto> lowStockProducts = getLowStockProducts();

        return DashboardDto.builder()
                .sales(salesSummary)
                .recentOrders(recentOrders)
                .lowStockProducts(lowStockProducts)
                .build();
    }

    private SalesSummaryDto getSalesSummary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.with(LocalTime.MIN);
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        LocalDateTime monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);

        BigDecimal todaySales = orderRepository.findTotalSalesBetween(todayStart, now);
        BigDecimal weekSales = orderRepository.findTotalSalesBetween(weekStart, now);
        BigDecimal monthSales = orderRepository.findTotalSalesBetween(monthStart, now);

        return SalesSummaryDto.builder()
                .today(todaySales)
                .thisWeek(weekSales)
                .thisMonth(monthSales)
                .build();
    }

    private List<OrderResponseDto> getRecentOrders() {
        List<Order> recentOrders = orderRepository.findTop5ByOrderByOrderDateDesc();
        return recentOrders.stream()
                .map(orderService::mapToOrderResponseDto) // This line will now work correctly
                .collect(Collectors.toList());
    }

    private List<LowStockProductDto> getLowStockProducts() {
        List<Product> products = productRepository.findByStockLessThan(LOW_STOCK_THRESHOLD);
        return products.stream()
                .map(product -> LowStockProductDto.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .stock(product.getStock())
                        .build())
                .collect(Collectors.toList());
    }
}
