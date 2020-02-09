package com.trendyol.shoppingcart.model;

public class Campaign extends Discount {
    private Category category;

    public Campaign(int minimumAmount, double discountAmount, DiscountType discountType) {
        super(minimumAmount, discountAmount, discountType);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
