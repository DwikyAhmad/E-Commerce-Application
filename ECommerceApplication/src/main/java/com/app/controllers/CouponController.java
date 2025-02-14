package com.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Coupon;
import com.app.payloads.CouponDTO;
import com.app.payloads.ResponseDTO;
import com.app.services.CouponService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/admin/coupon")
    public ResponseEntity<?> createCoupon(@RequestBody CouponDTO couponDTO) {
        Coupon coupon = couponService.createCoupon(couponDTO.getCode(), couponDTO.getDiscountPercentage());
        ResponseDTO response = new ResponseDTO("Coupon created successfully", coupon, 201);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/public/coupons")
    public ResponseEntity<?> getCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        ResponseDTO response = new ResponseDTO("All coupons fetched successfully", coupons, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/public/coupon/{id}")
    public ResponseEntity<?> getMethodName(@PathVariable Long id) {
        Coupon coupon = couponService.getCoupon(id);
        ResponseDTO response = new ResponseDTO("Coupon fetched successfully", coupon, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/public/coupon/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable Long id, @RequestBody CouponDTO couponDTO) {
        Coupon coupon = couponService.getCoupon(id);
        coupon.setCode(couponDTO.getCode());
        coupon.setDiscountPercentage(couponDTO.getDiscountPercentage());
        coupon = couponService.updateCoupon(id, coupon);

        ResponseDTO response = new ResponseDTO("Coupon updated successfully", coupon, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/coupon/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        ResponseDTO response = new ResponseDTO("Coupon deleted successfully", null, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
