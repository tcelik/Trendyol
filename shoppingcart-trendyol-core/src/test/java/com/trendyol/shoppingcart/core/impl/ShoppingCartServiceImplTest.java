package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.model.Category;
import com.trendyol.shoppingcart.model.Product;
import com.trendyol.shoppingcart.repository.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShoppingCartServiceImplTest {

    @Test
    public void test_think_database_just_a_map_instance() {
        HashMap<Product, Integer> productsWithQuantity = new HashMap<>();
        Product apple = new Product("Apple", 100.0, new Category("food"));

        productsWithQuantity.put(apple, 3);
        assertEquals(productsWithQuantity.get(apple), 3);
    }

    @Test
    public void test_products_hashmap_from_repository_layer() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository();
        assertNotNull(shoppingCartRepository.getProducts());

    }
    @Test
    public void test_adding_product_to_products_hashmap_in_repo_layer() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2);
        shoppingCartRepository.addItem(almonds, 5);

        assertEquals(shoppingCartRepository.getProducts().size(), 2);
    }

    @Test
    public void test_adding_same_product_with_different_quantity_expected_one_product() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2); // same object hash code provided by JVM.

        assertEquals(shoppingCartRepository.getProducts().size(), 1);

    }

    @Test
    public void test_adding_same_product_with_different_quantity_expected_total_quantity() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2);

        assertEquals(shoppingCartRepository.getProducts().get(apple), 12);

    }

    @Test
    public void test_adding_different_product() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 150.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(almonds, 2);

        assertEquals(shoppingCartRepository.getProducts().size(), 2);
        assertEquals(shoppingCartRepository.getProducts().get(apple), 10);
        assertEquals(shoppingCartRepository.getProducts().get(almonds), 2);
    }

    @Test
    public void test_adding_null_product() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = null;

        shoppingCartRepository.addItem(apple, 10);

        assertEquals(shoppingCartRepository.getProducts().size(), 0);
    }

    @Test
    public void test_adding_product_with_zero_quantity() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 0);

        assertEquals(shoppingCartRepository.getProducts().size(), 0);
    }

    @Test
    public void test_adding_product_with_negative_quantity__must_not_be_added() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, -1);

        assertEquals(shoppingCartRepository.getProducts().size(), 0);
    }

}
