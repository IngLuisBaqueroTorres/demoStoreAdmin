package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.CouponRequestDto;
import com.ingeduardo.demostore.dto.CouponResponseDto;
import com.ingeduardo.demostore.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@PreAuthorize("hasRole('ADMIN')")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponseDto> createCoupon(@RequestBody CouponRequestDto couponDto) {
        CouponResponseDto createdCoupon = couponService.createCoupon(couponDto);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {
        List<CouponResponseDto> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);    
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable Long id) {
        CouponResponseDto coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponseDto> getCouponByCode(@PathVariable String code) {
        CouponResponseDto coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable Long id, @RequestBody CouponRequestDto couponDto) {
        CouponResponseDto updatedCoupon = couponService.updateCoupon(id, couponDto);
        return ResponseEntity.ok(updatedCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}
