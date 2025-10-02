package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.SettingRequestDto;
import com.ingeduardo.demostore.dto.SettingResponseDto;
import com.ingeduardo.demostore.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_SETTINGS')")
    public ResponseEntity<SettingResponseDto> getSettings() {
        return ResponseEntity.ok(settingService.getSettings());
    }

    @PutMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_SETTINGS')")
    public ResponseEntity<SettingResponseDto> updateSettings(@RequestBody SettingRequestDto requestDto) {
        return ResponseEntity.ok(settingService.updateSettings(requestDto));
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasPermission(null, 'MANAGE_SETTINGS')")
    public ResponseEntity<SettingResponseDto> initializeSettings() {
        return new ResponseEntity<>(settingService.initializeSettings(), HttpStatus.CREATED);
    }
}
