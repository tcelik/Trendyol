package com.trendyol.shoppingcart.model;

import lombok.ToString;

import java.util.Optional;

@ToString
public class Category {
    private String title;
    private Optional<Category> parentCategory;

    public Category() {
        this(null);
    }

    public Category(String title) {
        this(title, Optional.empty());
    }

    public Category(String title, Optional<Category> parentCategory) {
        this.title = title;
        this.parentCategory = parentCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<Category> getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Optional<Category> parentCategory) {
        this.parentCategory = parentCategory;
    }
}
