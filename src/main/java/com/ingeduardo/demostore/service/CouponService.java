package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.CouponRequestDto;
import com.ingeduardo.demostore.dto.CouponResponseDto;

import java.util.List;

public interface CouponService {
    CouponResponseDto createCoupon(CouponRequestDto couponDto);
    List<CouponResponseDto> getAllCoupons();
    CouponResponseDto getCouponById(Long id);
    CouponResponseDto updateCoupon(Long id, CouponRequestDto couponDto);
    void deleteCoupon(Long id);
    CouponResponseDto getCouponByCode(String code);
}
