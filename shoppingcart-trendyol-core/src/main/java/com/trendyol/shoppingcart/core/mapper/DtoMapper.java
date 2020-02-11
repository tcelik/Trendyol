package com.trendyol.shoppingcart.core.mapper;

import com.trendyol.shoppingcart.api.ShoppingCartService;
import com.trendyol.shoppingcart.common.dto.ProductDto;
import com.trendyol.shoppingcart.model.Product;

public class DtoMapper {
    public static ProductDto toProducDto(Product product, ShoppingCartService cart) {
        ProductDto productDto = new ProductDto();

        productDto.setCategoryName(product.getCategory().getTitle());
        productDto.setProductName(product.getTitle());
        productDto.setQuantity(cart.getQuantityOfProduct(product));
        productDto.setUnitOfPrice(product.getPrice());
        productDto.setTotalPrice(product.getPrice() * cart.getQuantityOfProduct(product));

        return productDto;
    }
}
