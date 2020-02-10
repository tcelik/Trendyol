package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.api.ShoppingCartService;

public class DefaultDeliveryCostCalculatorServiceImpl extends DeliveryCostCalculatorServiceImpl {

    public DefaultDeliveryCostCalculatorServiceImpl(Double costPerDelivery, Double costPerProduct, Double fixedCost) {
        super(costPerDelivery, costPerProduct, fixedCost);
    }
}
