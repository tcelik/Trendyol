package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.api.ShoppingCartService;

public class DeliveryCostCalculatorServiceImpl implements DeliveryCostCalculatorService {
    private Double costPerDelivery, costPerProduct, fixedCost;

    public DeliveryCostCalculatorServiceImpl(Double costPerDelivery, Double costPerProduct, Double fixedCost)
    {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    @Override
    public Double calculateFor(ShoppingCartService cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null");
        }
        int numberOfDeliveries = cart.getNumberOfDeliveries();
        int numberOfProducts = cart.getNumberOfProducts();
        return (costPerDelivery * numberOfDeliveries) + (costPerProduct * numberOfProducts) + fixedCost;
    }
}
