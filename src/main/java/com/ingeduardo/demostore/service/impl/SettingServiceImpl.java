package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.SettingRequestDto;
import com.ingeduardo.demostore.dto.SettingResponseDto;
import com.ingeduardo.demostore.model.Setting;
import com.ingeduardo.demostore.repository.SettingRepository;
import com.ingeduardo.demostore.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    public SettingResponseDto getSettings() {
        Setting setting = settingRepository.findAll().stream().findFirst()
                .orElseGet(this::initializeDefaultSettings);
        return convertToDto(setting);
    }

    @Override
    public SettingResponseDto updateSettings(SettingRequestDto requestDto) {
        Setting setting = settingRepository.findAll().stream().findFirst()
                .orElseGet(this::initializeDefaultSettings);

        setting.setStoreName(requestDto.getStoreName());
        setting.setContactEmail(requestDto.getContactEmail());
        setting.setLogoUrl(requestDto.getLogoUrl());
        setting.setTaxRate(requestDto.getTaxRate());
        setting.setTermsAndConditions(requestDto.getTermsAndConditions());
        setting.setPrivacyPolicy(requestDto.getPrivacyPolicy());
        setting.setTimezone(requestDto.getTimezone());
        setting.setCurrency(requestDto.getCurrency());
        setting.setLanguage(requestDto.getLanguage());

        Setting updatedSetting = settingRepository.save(setting);
        return convertToDto(updatedSetting);
    }

    @Override
    public SettingResponseDto initializeSettings() {
        Setting setting = initializeDefaultSettings();
        return convertToDto(setting);
    }

    private Setting initializeDefaultSettings() {
        Setting defaultSetting = new Setting();
        defaultSetting.setStoreName("My Universal Store");
        defaultSetting.setContactEmail("contact@universalstore.com");
        defaultSetting.setLogoUrl("");
        defaultSetting.setTaxRate(BigDecimal.valueOf(0.0));
        defaultSetting.setTermsAndConditions("Default terms and conditions...");
        defaultSetting.setPrivacyPolicy("Default privacy policy...");
        defaultSetting.setTimezone("UTC");
        defaultSetting.setCurrency("USD");
        defaultSetting.setLanguage("en");
        return settingRepository.save(defaultSetting);
    }

    private SettingResponseDto convertToDto(Setting setting) {
        SettingResponseDto dto = new SettingResponseDto();
        dto.setId(setting.getId());
        dto.setStoreName(setting.getStoreName());
        dto.setContactEmail(setting.getContactEmail());
        dto.setLogoUrl(setting.getLogoUrl());
        dto.setTaxRate(setting.getTaxRate());
        dto.setTermsAndConditions(setting.getTermsAndConditions());
        dto.setPrivacyPolicy(setting.getPrivacyPolicy());
        dto.setTimezone(setting.getTimezone());
        dto.setCurrency(setting.getCurrency());
        dto.setLanguage(setting.getLanguage());
        return dto;
    }
}
