package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.api.ShoppingCartService;
import com.trendyol.shoppingcart.model.Campaign;
import com.trendyol.shoppingcart.model.Coupon;
import com.trendyol.shoppingcart.model.Product;
import com.trendyol.shoppingcart.repository.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

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
        log.info("Getting initial value price of all products in cart");
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
        this.deliveryCostCalculator = new DefaultDeliveryCostCalculatorServiceImpl(1.0, 1.0, 2.99);
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
    public Double getTotalAmountAfterDiscounts() {
        Double totalAmount = getTotalPriceOfProducts();
        totalAmount -= shoppingCartRepository.getCampaignDiscountPrice(); // first campaing
        totalAmount -= shoppingCartRepository.getCouponDiscount(totalAmount);
        return totalAmount;
    }

    public Integer getNumberOfDeliveries() {
        return shoppingCartRepository.getDistinctCategoryValue();
    }

    public void setDeliveryCostCalculator(DeliveryCostCalculatorService deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }
}
