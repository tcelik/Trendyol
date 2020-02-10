package com.trendyol.shoppingcart.api;

import com.trendyol.shoppingcart.model.Campaign;
import com.trendyol.shoppingcart.model.Coupon;
import com.trendyol.shoppingcart.model.Product;

import java.util.List;

public interface ShoppingCartService {
    /**
     * Adding item ( product ) to hash-map
     */
    void addItem(Product product, int quantity);

    /**
     * Total price of products in cart without discount ( campaings | coupons )
     */
    Double getTotalPriceOfProducts();

    /**
     * How many products in cart?
     */
    Integer getNumberOfProducts();

    /**
     * Quantity info ( amount ) of product
     * Ex: Apple -> 3
     */
    Integer getQuantityOfProduct(Product product);

    /**
     * Adding campaign(s) to cart for apply discount related to given dynamic campaign format.
     */
    void addingCampaignsToCart(List<Campaign> asList);

    /**
     * Get all campaings this cart
     * @return
     */
    List<Campaign> getAllCampaigns();


    /**
     * Apply discounts with campaigns list
     *
     */
    void applyDiscounts(List<Campaign> campaigns);

    /**
     * Getting campaings discount
     *
     */
    Double getCampaignDiscount();

    /**
     * Apply coupon to cart if applicable.
     * Throws exception if it is not applicable.
     * Not applicable means: total cart price minumum | null coupon
     */
    void applyCoupon(Coupon coupon) throws IllegalStateException;

    /**
     * Getting coupon discount price
     * As a rule: First applied campaign,
     *
     */
    Double getCouponDiscount();

    /**
     * Final price by customer paying.
     * Getting final cart total price
     * As a rule: First applied campaign and then coupon.
     *
     */
    Double getTotalAmountAfterDiscounts();

    /**
     * Getting delivery cost calculation
     */
    Double getDeliveryCost();

    Integer getNumberOfDeliveries();

}
