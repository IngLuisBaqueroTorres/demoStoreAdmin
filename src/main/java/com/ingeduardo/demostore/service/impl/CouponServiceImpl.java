package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.CouponRequestDto;
import com.ingeduardo.demostore.dto.CouponResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Coupon;
import com.ingeduardo.demostore.repository.CouponRepository;
import com.ingeduardo.demostore.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponResponseDto createCoupon(CouponRequestDto couponDto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDto, coupon);
        coupon.setTimesUsed(0);
        Coupon savedCoupon = couponRepository.save(coupon);
        return convertToDto(savedCoupon);
    }

    @Override
    public List<CouponResponseDto> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponseDto getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        return convertToDto(coupon);
    }

    @Override
    public CouponResponseDto updateCoupon(Long id, CouponRequestDto couponDto) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        BeanUtils.copyProperties(couponDto, coupon);
        Coupon updatedCoupon = couponRepository.save(coupon);
        return convertToDto(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public CouponResponseDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));
        return convertToDto(coupon);
    }

    private CouponResponseDto convertToDto(Coupon coupon) {
        CouponResponseDto dto = new CouponResponseDto();
        BeanUtils.copyProperties(coupon, dto);
        return dto;
    }
}
