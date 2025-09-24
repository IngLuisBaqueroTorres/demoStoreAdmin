package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.SettingRequestDto;
import com.ingeduardo.demostore.dto.SettingResponseDto;

public interface SettingService {
    SettingResponseDto getSettings();
    SettingResponseDto updateSettings(SettingRequestDto requestDto);
    SettingResponseDto initializeSettings();
}
