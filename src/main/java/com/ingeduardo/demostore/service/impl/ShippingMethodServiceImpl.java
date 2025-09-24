package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.ShippingMethodRequestDto;
import com.ingeduardo.demostore.dto.ShippingMethodResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.ShippingMethod;
import com.ingeduardo.demostore.repository.ShippingMethodRepository;
import com.ingeduardo.demostore.service.ShippingMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingMethodServiceImpl implements ShippingMethodService {

    private final ShippingMethodRepository shippingMethodRepository;

    public ShippingMethodServiceImpl(ShippingMethodRepository shippingMethodRepository) {
        this.shippingMethodRepository = shippingMethodRepository;
    }

    @Override
    public List<ShippingMethodResponseDto> getAllShippingMethods() {
        return shippingMethodRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShippingMethodResponseDto getShippingMethodById(Long id) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod not found with id: " + id));
        return convertToDto(shippingMethod);
    }

    @Override
    public ShippingMethodResponseDto createShippingMethod(ShippingMethodRequestDto requestDto) {
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setName(requestDto.getName());
        shippingMethod.setDescription(requestDto.getDescription());
        shippingMethod.setCost(requestDto.getCost());
        ShippingMethod savedMethod = shippingMethodRepository.save(shippingMethod);
        return convertToDto(savedMethod);
    }

    @Override
    public ShippingMethodResponseDto updateShippingMethod(Long id, ShippingMethodRequestDto requestDto) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod not found with id: " + id));
        shippingMethod.setName(requestDto.getName());
        shippingMethod.setDescription(requestDto.getDescription());
        shippingMethod.setCost(requestDto.getCost());
        ShippingMethod updatedMethod = shippingMethodRepository.save(shippingMethod);
        return convertToDto(updatedMethod);
    }

    @Override
    public void deleteShippingMethod(Long id) {
        if (!shippingMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException("ShippingMethod not found with id: " + id);
        }
        shippingMethodRepository.deleteById(id);
    }

    private ShippingMethodResponseDto convertToDto(ShippingMethod shippingMethod) {
        ShippingMethodResponseDto dto = new ShippingMethodResponseDto();
        dto.setId(shippingMethod.getId());
        dto.setName(shippingMethod.getName());
        dto.setDescription(shippingMethod.getDescription());
        dto.setCost(shippingMethod.getCost());
        return dto;
    }
}
