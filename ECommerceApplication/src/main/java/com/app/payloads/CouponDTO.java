package com.app.payloads;

import lombok.Data;

@Data
public class CouponDTO {
    private String code;
    private double discountPercentage;
}
