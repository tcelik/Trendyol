package com.trendyol.shoppingcart.model;

public abstract class Discount {
    private Integer minimumAmount;
    private Double discountAmount;
    private DiscountType discountType;

    public Discount(Integer minimumAmount, Double discountAmount, DiscountType discountType) {
        this.minimumAmount = minimumAmount;
        this.discountAmount = discountAmount;
        this.discountType = discountType;
    }

    public enum DiscountType {
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
