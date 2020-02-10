package com.trendyol.shoppingcart.model;

public class Coupon extends Discount {

    public Coupon(Integer minimumAmount, Double discountAmount, DiscountType discountType) {
        super(minimumAmount, discountAmount, discountType);
    }

    public boolean isApplicableForCart(Double totalCartPrice) {
        return totalCartPrice >= super.getMinimumAmount();

    }
}
