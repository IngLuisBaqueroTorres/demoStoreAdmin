package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.DashboardDto;
import com.ingeduardo.demostore.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboardData() {
        DashboardDto dashboardData = dashboardService.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }
}
