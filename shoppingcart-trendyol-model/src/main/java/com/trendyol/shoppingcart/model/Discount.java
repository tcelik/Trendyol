package com.trendyol.shoppingcart.model;

public abstract class Discount {
    private Double discountAmount;
    private DiscountType discountType;
    private Integer minimumAmount;

    public Discount(int minimumAmount, double discountAmount, DiscountType discountType) {
        this.discountAmount = discountAmount;
        this.discountType = discountType;
        this.minimumAmount = minimumAmount;
    }

    public static enum DiscountType {
        RATE,
        AMOUNT
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Integer getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Integer minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}
