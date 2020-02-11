package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.api.ShoppingCartService;
import com.trendyol.shoppingcart.common.dto.ProductDto;
import com.trendyol.shoppingcart.core.mapper.DtoMapper;
import com.trendyol.shoppingcart.model.Campaign;
import com.trendyol.shoppingcart.model.Coupon;
import com.trendyol.shoppingcart.model.Product;
import com.trendyol.shoppingcart.repository.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository();
    private DeliveryCostCalculatorService deliveryCostCalculator;

    public ShoppingCartServiceImpl(DeliveryCostCalculatorService deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    @Override
    public void addItem(Product product, int quantity) {
        if (quantity > 0) log.info("Adding {} product: {}", quantity, product);
        shoppingCartRepository.addItem(product, quantity);
        if (quantity > 0) log.info("Added {} product: {}", quantity, product);
    }

    @Override
    public Double getTotalPriceOfProducts() {
        // log.info("Getting initial value price of all products in cart");
        return shoppingCartRepository.getTotalPriceOfAllProducts();
    }

    @Override
    public Integer getNumberOfProducts() {
        return shoppingCartRepository.get();
    }

    @Override
    public Integer getQuantityOfProduct(Product product) {
        return shoppingCartRepository.getQuantityOfProduct(product);
    }

    @Override
    public void addingCampaignsToCart(List<Campaign> campaigns) {
        shoppingCartRepository.getCampaigns().addAll(campaigns);
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return shoppingCartRepository.getCampaigns();
    }

    @Override
    public void applyDiscounts(List<Campaign> campaigns) {
        this.addingCampaignsToCart(campaigns);

    }

    public void applyDiscounts(Campaign... campaigns) {
        this.addingCampaignsToCart(Arrays.asList(campaigns));

    }

    @Override
    public Double getCampaignDiscount() {
        return shoppingCartRepository.getCampaignDiscountPrice();
    }

    @Override
    public Double getDeliveryCost() {
        if (deliveryCostCalculator == null) {
            setTheDefaultDeliveryCostCalculator();
            return deliveryCostCalculator.calculateFor(this);
        }
        return deliveryCostCalculator.calculateFor(this);
    }

    private void setTheDefaultDeliveryCostCalculator() {
        this.deliveryCostCalculator = new FixedDeliveryCostCalculatorServiceImpl(1.0, 1.0);
    }

    @Override
    public void applyCoupon(Coupon coupon) {
        shoppingCartRepository.applyCoupon(coupon);
    }

    @Override
    public Double getCouponDiscount() {
        return shoppingCartRepository.getCouponDiscount(getTotalPriceOfProducts());
    }

    @Override
    public Double getTotalAmountAfterDiscounts() { // campaign, coupons applied.
        Double totalAmount = getTotalPriceOfProducts();
        totalAmount -= shoppingCartRepository.getCampaignDiscountPrice(); // first campaign
        totalAmount -= shoppingCartRepository.getCouponDiscount(totalAmount);
        return totalAmount;
    }

    public Integer getNumberOfDeliveries() {
        return shoppingCartRepository.getDistinctCategoryValue();
    }

    public void setDeliveryCostCalculator(DeliveryCostCalculatorService deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    public String print()
    {
        StringBuilder builder = new StringBuilder();
        HashMap<Product, Integer> products = shoppingCartRepository.getProducts();
        Map<String, List<Product>> productsGroupByTitle = products.keySet().stream().collect(Collectors.groupingBy(p -> p.getCategory().getTitle()));

        builder.append(String.format("%15s %15s %15s %15s %15s%n", "Category Name", "Product Name", "Quantity", "Unit Price", "Total Price"));
        builder.append("---------------------------------------------------------------------------------------\n");

        /**
         *  Info: This algorithm java8 version. In there, I prefering the hard-coded-way just readability concerns.
         *
         *  productsGroupByTitle.keySet().forEach(key -> productsGroupByTitle.get(key).stream().map(product -> DtoMapper.toProducDto(product, this)).map(ProductDto::toString).forEachOrdered(builder::append));
         */
        for (String category : productsGroupByTitle.keySet()) {
            for (Product product : productsGroupByTitle.get(category)) {
                ProductDto productDto = DtoMapper.toProducDto(product, this);
                builder.append(productDto.toString());
            }
        }

        builder.append("---------------------------------------------------------------------------------------\n");
        builder.append("Results:\n");
        builder.append(String.format("Total Amount: %.3f%n", getTotalPriceOfProducts()));
        builder.append(String.format("Total Amount After Discounts: %.3f%n", getTotalAmountAfterDiscounts()));
        builder.append(String.format("Your Delivery Cost: %.3f%n", getDeliveryCost()));
        builder.append(String.format("Your Total Payment: %.3f%n", getTotalAmountAfterDiscounts() - getDeliveryCost()));
        //builder.append("Total Discount: " + (getTotalPriceOfProducts() - getTotalAmountAfterDiscounts()) + "\n");

        return builder.toString();
    }


}
