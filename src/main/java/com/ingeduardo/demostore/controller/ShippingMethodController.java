package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.ShippingMethodRequestDto;
import com.ingeduardo.demostore.dto.ShippingMethodResponseDto;
import com.ingeduardo.demostore.service.ShippingMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-methods")
public class ShippingMethodController {

    private final ShippingMethodService shippingMethodService;

    public ShippingMethodController(ShippingMethodService shippingMethodService) {
        this.shippingMethodService = shippingMethodService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_SHIPPING_METHODS')")
    public ResponseEntity<List<ShippingMethodResponseDto>> getAllShippingMethods() {
        return ResponseEntity.ok(shippingMethodService.getAllShippingMethods());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_SHIPPING_METHODS')")
    public ResponseEntity<ShippingMethodResponseDto> getShippingMethodById(@PathVariable Long id) {
        return ResponseEntity.ok(shippingMethodService.getShippingMethodById(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_SHIPPING_METHODS')")
    public ResponseEntity<ShippingMethodResponseDto> createShippingMethod(@RequestBody ShippingMethodRequestDto requestDto) {
        ShippingMethodResponseDto createdMethod = shippingMethodService.createShippingMethod(requestDto);
        return new ResponseEntity<>(createdMethod, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_SHIPPING_METHODS')")
    public ResponseEntity<ShippingMethodResponseDto> updateShippingMethod(@PathVariable Long id, @RequestBody ShippingMethodRequestDto requestDto) {
        return ResponseEntity.ok(shippingMethodService.updateShippingMethod(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_SHIPPING_METHODS')")
    public ResponseEntity<Void> deleteShippingMethod(@PathVariable Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return ResponseEntity.noContent().build();
    }
}
