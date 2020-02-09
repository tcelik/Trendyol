package com.trendyol.shoppingcart.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductTest {

    @Test
    public  void test_constructor_product_with_no_arguments() {
        Product product = new Product();
    }

    @Test
    public  void test_constructor_product_with_title() {
        String title = "title";

        Product product = new Product(title);

        assertEquals(product.getTitle(), "title");
    }

    @Test
    public  void test_constructor_product_with_title_price() {
        String title = "title";
        Double price = 10.0;

        Product product = new Product(title, price);
        Assertions.assertEquals(product.getPrice(), 10.0);
    }

    @Test
    public  void test_constructor_product_with_title_price_and_category_field() {
        String title = "title";
        Double price = 10.0;
        Category category = new Category();

        Product product = new Product(title, price, category);

        assertNotNull(product.getCategory());
    }
}
