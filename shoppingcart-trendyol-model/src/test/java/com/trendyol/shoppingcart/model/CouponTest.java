package com.trendyol.shoppingcart.model;

import org.junit.jupiter.api.Test;

import static com.trendyol.shoppingcart.model.Discount.DiscountType.RATE;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    public void contructorCouponTest_success()
    {
        Coupon coupon = new Coupon(100, 10.0, RATE);
        assertEquals(coupon.getMinimumAmount(), 100);
        assertEquals(coupon.getDiscountAmount(), 10.0);
        assertEquals(coupon.getDiscountType(), RATE);
    }
}
