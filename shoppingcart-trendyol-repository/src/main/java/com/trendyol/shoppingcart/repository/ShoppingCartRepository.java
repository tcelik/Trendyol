package com.trendyol.shoppingcart.repository;

import com.trendyol.shoppingcart.model.*;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ShoppingCartRepository {
    private HashMap<Product, Integer> products; // products with quantity hash map using like a database.
    private List<Campaign> campaigns;
    private Coupon coupon;

    public ShoppingCartRepository() {
        this.products = new HashMap<>();
        this.campaigns = new ArrayList<>();
    }

    // ### Private Methods ###
    private HashMap<Product, Integer> getProductsByCategory(Category category) {
        HashMap<Product, Integer> resultProducts = new HashMap<>();

        for (Product p : products.keySet()) {
            if (p.getCategory().getTitle().equals(category.getTitle())) {
                resultProducts.put(p, products.get(p));
            }
        }
        return resultProducts;
    }

    private Integer getAllProductQuantityCountByCategory(Category category) {
        HashMap<Product, Integer> productsByCategory = getProductsByCategory(category);
        return productsByCategory.values().stream().reduce(0, Integer::sum);
    }

    private Double applyCampaign() {
        Double resultDiscountPrice = 0.0;
        Double tempDiscountPrice = 0.0;

        for (Campaign campaign : campaigns) {

            switch (campaign.getDiscountType()) {
                case RATE:
                    tempDiscountPrice = processDiscountTypeRateCampaign(campaign);
                    if (tempDiscountPrice > resultDiscountPrice) resultDiscountPrice = tempDiscountPrice;
                    break;
                case AMOUNT:
                    tempDiscountPrice = processDiscountTypeAmountCampaign(campaign);
                    resultDiscountPrice += tempDiscountPrice;
                    break;
                default: log.info("Not valid campaign, valid for campaign RATE | AMOUNT");
            }
        }

        return resultDiscountPrice;
    }

    private Double processDiscountTypeAmountCampaign(Campaign campaign) {
        Category campaignCategory = campaign.getCategory();
        Double discountAmount = campaign.getDiscountAmount(); // 5 TL

        if ( getAllProductQuantityCountByCategory(campaignCategory) >= campaign.getMinimumAmount()) {
            return discountAmount;
        }
        else {
            return 0.0;
        }
    }

    private Double processDiscountTypeRateCampaign(Campaign campaign) {
        Category campaignCategory = campaign.getCategory();
        Double discountRate = campaign.getDiscountAmount();

        if ( getAllProductQuantityCountByCategory(campaignCategory) >= campaign.getMinimumAmount()) {
            Double totalPriceOfAllProducts = getTotalPriceOfAllProducts();
            return (totalPriceOfAllProducts * discountRate) / 100.0;
        } else {
            return 0.0;
        }
    }

    // ### Public Methods ###
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

    public Double getTotalPriceOfAllProducts() {
        Double total = 0.0;

        for (Product p : products.keySet()) {
            Integer quantityOfProduct = products.get(p);
            Double priceOfProduct = p.getPrice();
            total += quantityOfProduct * priceOfProduct;
        }

        return total;
    }

    public Double getCampaignDiscountPrice() { // Kampanya indirimi ne kadar?
        return applyCampaign();
    }

    public Integer get() { // how many products?
        return products.size();
    }

    public Integer getQuantityOfProduct(Product product) {
        if (!products.containsKey(product)) {
            log.info("Product not found in this cart: {}", product);
            return null;
        }
        return products.get(product);
    }

    public void applyCoupon(Coupon coupon) {
        if (coupon == null) {
            log.info("Coupon must not be null");
            throw new IllegalStateException("Coupon must not be null");
        }

        if (!coupon.isApplicableForCart(getTotalPriceOfAllProducts())) {
            log.info("Coupon: {} is not applicable for this cart");
            throw new IllegalStateException("Coupon is not applicable for this cart");
        }
        this.coupon = coupon;
    }

    public Double getCouponDiscount(Double totalAmount) {
        Double resultCouponDiscount = 0.0;

        if (coupon != null && coupon.isApplicableForCart(getTotalPriceOfAllProducts())) {
            switch (coupon.getDiscountType()) {
                case RATE:
                    resultCouponDiscount = (totalAmount * coupon.getDiscountAmount()) / 100.0;
                    break;
                case AMOUNT:
                    return coupon.getDiscountAmount();
                default:
                    log.info("Please provide discount type AMOUNT | RATE");
                    break;
            }
        }

        return resultCouponDiscount;
    }
    // ### Getters / Setters  ###
    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
