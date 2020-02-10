package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.api.ShoppingCartService;

public class DeliveryCostCalculatorServiceImpl implements DeliveryCostCalculatorService {
    @Override
    public Double calculateFor(ShoppingCartService shoppingCartService) {
        return shoppingCartService.getDeliveryCost();
    }
}
