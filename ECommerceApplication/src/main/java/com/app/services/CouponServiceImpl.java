package com.app.services;

import com.app.entites.Coupon;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.repositories.CouponRepo;
import com.app.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Coupon createCoupon(String couponName, double discountPercentage) {
        if (couponRepo.findByCode(couponName).isPresent()) {
            throw new APIException("Coupon already exists with coupon code: " + couponName + " !!!");
        }

        List<Product> products = new ArrayList<>();
    
        Coupon coupon = new Coupon();
        coupon.setCode(couponName);
        coupon.setDiscountPercentage(discountPercentage);
        coupon.setProducts(products);

        return couponRepo.save(coupon);
    }

    @Override
    public Coupon getCoupon(Long couponId) {
        return couponRepo.findByCouponId(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "couponId", couponId));
    }

    @Override
    public Coupon getCouponWithCode(String couponCode) {
        return couponRepo.findByCode(couponCode).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "code", couponCode));
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    public Coupon updateCoupon(Long couponId, Coupon coupon) {
        Coupon couponToUpdate = couponRepo.findByCouponId(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "couponId", couponId));

        couponToUpdate.setCode(coupon.getCode());
        couponToUpdate.setDiscountPercentage(coupon.getDiscountPercentage());

        return couponRepo.save(couponToUpdate);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        Coupon couponToDelete = couponRepo.findByCouponId(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "couponId", couponId));

        List<Product> products = couponToDelete.getProducts();

        for (Product product : products) {
            product.setCoupon(null);
            productRepo.save(product);
        }

        couponRepo.delete(couponToDelete);
    }

    @Override
    public Coupon addProductToCoupon(Long couponId, Product product) {
        Long productId = product.getProductId();
        product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId));

        Coupon coupon = couponRepo.findByCouponId(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "couponId", couponId));

        boolean isProductNotPresent = true;
        List<Product> products = coupon.getProducts();

        for (Product listProduct : products) {
            if (listProduct.getProductName().equals(product.getProductName())
                    && listProduct.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            products.add(product);
            coupon.setProducts(products);

            product.setCoupon(coupon);
            productRepo.save(product);

            return couponRepo.save(coupon);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public Coupon deleteProductFromCoupon(Long couponId, Product product) {
        Long productId = product.getProductId();
        product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId));

        Coupon coupon = couponRepo.findByCouponId(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Coupon", "couponId", couponId));

        boolean isProductNotPresent = true;
        List<Product> products = coupon.getProducts();

        for (Product listProduct : products) {
            if (listProduct.getProductName().equals(product.getProductName())
                    && listProduct.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (!isProductNotPresent) {
            products.remove(product);
            coupon.setProducts(products);

            product.setCoupon(null);
            productRepo.save(product);

            return couponRepo.save(coupon);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }
}
