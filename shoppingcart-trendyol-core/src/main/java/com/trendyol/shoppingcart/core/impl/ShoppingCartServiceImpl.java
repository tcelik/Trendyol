package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.ShoppingCartService;
import com.trendyol.shoppingcart.model.Product;
import com.trendyol.shoppingcart.repository.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository();

    @Override
    public void addItem(Product product, int quantity) {
        log.info("Adding {} product: {}", quantity, product);
        shoppingCartRepository.addItem(product, quantity);
        log.info("Added {} product: {}", quantity, product);
    }
}
