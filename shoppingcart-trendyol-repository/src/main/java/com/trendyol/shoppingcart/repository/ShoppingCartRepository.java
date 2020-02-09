package com.trendyol.shoppingcart.repository;

import com.trendyol.shoppingcart.model.Product;
import java.util.HashMap;

public class ShoppingCartRepository {
    private HashMap<Product, Integer> products; // products with quantity hash map using like a database.

    public ShoppingCartRepository() {
        this.products = new HashMap<>();
    }

    public void addItem(Product product, Integer quantity) {
        if (product != null && quantity > 0) {

            if (products.containsKey(product)) {
                Integer currentAmount = products.get(product);
                products.put(product, currentAmount + quantity);
                return;
            }
            products.put(product, quantity);
        }
    }

    public void removeItem(Product product, Integer quantity) {
        if (product != null && quantity != 0 && products.containsKey(product)) {
            Integer currentAmount = products.get(product);
            products.put(product, currentAmount - quantity);
        }
    }

    public int get() {
        return products.size();
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }
}
