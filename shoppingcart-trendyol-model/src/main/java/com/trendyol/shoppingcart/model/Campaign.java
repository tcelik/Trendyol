package com.trendyol.shoppingcart.model;

import lombok.ToString;

@ToString
public class Campaign extends Discount {
    private Category category;

    public Campaign(Category category, double discountAmount, int minimumAmount, DiscountType rate) {
        super(minimumAmount, discountAmount, rate);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
