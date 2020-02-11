package com.trendyol.shoppingcart.core.impl;

public class FixedDeliveryCostCalculatorServiceImpl extends DeliveryCostCalculatorServiceImpl {

    public FixedDeliveryCostCalculatorServiceImpl(Double costPerDelivery, Double costPerProduct) {
        super(costPerDelivery, costPerProduct, 2.99);
    }
}
