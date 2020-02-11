package com.trendyol.shoppingcart.common.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String categoryName;
    private String productName;
    private Integer quantity;
    private Double unitOfPrice;
    private Double totalPrice;

    @Override
    public String toString()
    {
        return String.format("%15s %15s %15d %15.2f %15.2f%n", categoryName, productName, quantity, unitOfPrice, totalPrice);
    }
}
