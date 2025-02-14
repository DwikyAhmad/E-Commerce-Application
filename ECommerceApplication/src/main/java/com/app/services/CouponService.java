package com.app.services;


import com.app.entites.Coupon;
import com.app.entites.Product;

import java.util.List;

public interface CouponService {
    Coupon createCoupon(Long couponId, String couponName, double discountPercentage, List<Product> products);

    Coupon getCoupon(Long couponId);

    Coupon getCouponWithCode(String couponCode);

    List<Coupon> getAllCoupons();

    Coupon updateCoupon(Long couponId, Coupon coupon);

    void deleteCoupon(Long couponId);

    Coupon addProductToCoupon(Long couponId, Product product);

    Coupon deleteProductFromCoupon(Long couponId, Product product);
}
