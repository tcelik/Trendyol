package com.trendyol.shoppingcart.model;

public class Coupon extends Discount {

    public Coupon(int minimumAmount, double discountAmount, DiscountType discountType) {
        super(minimumAmount, discountAmount, discountType);
    }
}
