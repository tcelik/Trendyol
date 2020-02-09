package com.trendyol.shoppingcart.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    public void test_constructor_category_with_no_arguments() {
        Category category = new Category();

        assertNotNull(category.getParentCategory()); // not null but optional empty
    }

    @Test
    public void test_constructor_category_with_title() {
        String title = "category-title";

        Category category = new Category(title);

        assertEquals(category.getTitle(), title);
        assertEquals(category.getParentCategory().isPresent(), false);
    }

    @Test
    public void test_constructor_category_with_title_and_parent_category() {
        String title = "category-title";
        Category parentCategory = new Category("parent-category");

        Category subCategoryUnderTest = new Category(title, Optional.of(parentCategory));

        assertTrue(subCategoryUnderTest.getParentCategory().isPresent());
        assertEquals(subCategoryUnderTest.getParentCategory().get().getTitle(), parentCategory.getTitle());
    }
}
