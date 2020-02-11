package com.trendyol.shoppingcart.main;

import com.trendyol.shoppingcart.api.ShoppingCartService;
import com.trendyol.shoppingcart.core.impl.DeliveryCostCalculatorServiceImpl;
import com.trendyol.shoppingcart.core.impl.ShoppingCartServiceImpl;
import com.trendyol.shoppingcart.model.*;

import java.util.Arrays;
import java.util.Optional;

public class ShoppingCartApplication {
    public static void main(String[] args) {
        Category fruit = new Category("Fruit");
        Category electronic = new Category("Electronic");
        Category computer = new Category("Computer", Optional.of(electronic));

        Product apple = new Product("Apple", 100.0, fruit);
        Product orange = new Product("Orange", 100.0, fruit);
        Product macBookPro = new Product("Mac Book PRO", 5000.0, computer);

        ShoppingCartService cart = new ShoppingCartServiceImpl(new DeliveryCostCalculatorServiceImpl(1.0, 2.0, 2.99));
        cart.addItem(apple, 10);
        cart.addItem(orange, 10);
        cart.addItem(macBookPro, 1);

        Campaign c1 = new Campaign(fruit, 10, 10, Discount.DiscountType.RATE);
        Campaign c2 = new Campaign(computer, 10, 1, Discount.DiscountType.RATE);
        cart.applyDiscounts(Arrays.asList(c1, c2));

        Coupon coupon = new Coupon(2000, 0.0, Discount.DiscountType.AMOUNT);
        cart.applyCoupon(coupon);

        System.out.println(cart.print());
    }
}
