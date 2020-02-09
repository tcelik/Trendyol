package com.trendyol.shoppingcart.api;

import com.trendyol.shoppingcart.model.Product;

public interface ShoppingCartService {
    void addItem(Product product, int quantity);
}
