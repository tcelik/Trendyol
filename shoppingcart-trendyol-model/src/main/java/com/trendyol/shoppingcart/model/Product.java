package com.trendyol.shoppingcart.model;

public class Product {
    private String title;
    private Double price;
    private Category category;

    public Product() {

    }

    public Product(String title) {
        this(title, null);
    }

    public Product(String title, Double price) {
        this(title, price, null);
    }

    public Product(String title, Double price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
