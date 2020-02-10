package com.trendyol.shoppingcart.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.trendyol.shoppingcart.model.Discount.DiscountType.*;
import static org.junit.jupiter.api.Assertions.*;

class CampaignTest {

    @Test
    void test_constructor_campaign_with_category_and_info_related_discount_quantity() {

        /**
         * This campaing applied to <"food"> category
         * If the cart has a minimum <3> product of <"food"> category then applied, <%20>
         */
        Campaign sampleCampaign = new Campaign(new Category("food"), 20.0, 3, RATE);

        assertNotNull(sampleCampaign.getCategory());
        assertEquals(sampleCampaign.getMinimumAmount(), 3);
        assertEquals(sampleCampaign.getDiscountAmount(), 20.0);
        assertEquals(sampleCampaign.getDiscountType(), Discount.DiscountType.valueOf("RATE"));
    }


    @Test
    void test_adding_campaign_to_campaign_list() {
        Campaign campaign1 = new Campaign(new Category("x"), 20.0, 3, Discount.DiscountType.RATE);
        Campaign campaign2 = new Campaign(new Category("y"), 50.0, 5, Discount.DiscountType.RATE);
        Campaign campaign3 = new Campaign(new Category("z"), 5, 10, Discount.DiscountType.AMOUNT);

        List<Campaign> campaigns = Arrays.asList(campaign1, campaign2, campaign3);
        assertEquals(campaigns.size(), 3);

    }
}
