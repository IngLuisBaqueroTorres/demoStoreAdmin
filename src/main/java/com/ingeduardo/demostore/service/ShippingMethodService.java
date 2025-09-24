package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.ShippingMethodRequestDto;
import com.ingeduardo.demostore.dto.ShippingMethodResponseDto;

import java.util.List;

public interface ShippingMethodService {
    List<ShippingMethodResponseDto> getAllShippingMethods();
    ShippingMethodResponseDto getShippingMethodById(Long id);
    ShippingMethodResponseDto createShippingMethod(ShippingMethodRequestDto requestDto);
    ShippingMethodResponseDto updateShippingMethod(Long id, ShippingMethodRequestDto requestDto);
    void deleteShippingMethod(Long id);
}
