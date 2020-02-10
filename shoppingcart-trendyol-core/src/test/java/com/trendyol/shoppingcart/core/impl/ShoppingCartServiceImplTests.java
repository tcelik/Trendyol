package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.DeliveryCostCalculatorService;
import com.trendyol.shoppingcart.model.*;
import com.trendyol.shoppingcart.repository.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.HashMap;
import static com.trendyol.shoppingcart.model.Discount.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ShoppingCartServiceImpl}.
 *
 * @author Tuğberk Çelik
 */
@Slf4j
public class ShoppingCartServiceImplTests {
    DeliveryCostCalculatorService calculator;
    ShoppingCartServiceImpl cart;

    @BeforeEach
    public void setup() {
        calculator = mock(DeliveryCostCalculatorService.class);
        cart = new ShoppingCartServiceImpl();
    }


    @Test
    public void test_think_database_just_a_map_instance() {
        HashMap<Product, Integer> productsWithQuantity = new HashMap<>();
        Product apple = new Product("Apple", 100.0, new Category("food"));

        productsWithQuantity.put(apple, 3);
        assertEquals(productsWithQuantity.get(apple), 3);
    }

    @Test
    public void test_products_hashmap_from_repository_layer() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository();
        assertNotNull(shoppingCartRepository.getProducts());

    }
    @Test
    public void test_adding_product_to_products_hashmap_in_repo_layer() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2);
        shoppingCartRepository.addItem(almonds, 5);

        assertEquals(shoppingCartRepository.getProducts().size(), 2);
    }

    @Test
    public void test_adding_same_product_with_different_quantity_expected_one_product() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2); // same object hash code provided by JVM.

        assertEquals(shoppingCartRepository.getProducts().size(), 1);

    }

    @Test
    public void test_adding_same_product_with_different_quantity_expected_total_quantity() {
        ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartRepository.addItem(apple, 10);
        shoppingCartRepository.addItem(apple, 2);

        assertEquals(shoppingCartRepository.getProducts().get(apple), 12);

    }

    @Test
    public void test_adding_different_product() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 150.0, new Category("food"));

        shoppingCartService.addItem(apple, 10);
        shoppingCartService.addItem(almonds, 2);

        assertEquals(shoppingCartService.getTotalProduct(), 2);
        assertEquals(shoppingCartService.getQuantityOfProduct(apple), 10);
        assertEquals(shoppingCartService.getQuantityOfProduct(almonds), 2);
    }

    @Test
    public void test_adding_null_product() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // empty products
        Product apple = null;

        shoppingCartService.addItem(apple, 10);

        assertEquals(shoppingCartService.getTotalProduct(), 0);
    }

    @Test
    public void test_adding_product_with_zero_quantity__must_not_be_added() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartService.addItem(apple, 0);

        assertEquals(shoppingCartService.getTotalProduct(), 0);
    }

    @Test
    public void test_adding_product_with_negative_quantity__must_not_be_added() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));

        shoppingCartService.addItem(apple, -1);

        assertEquals(shoppingCartService.getTotalProduct(), 0);
    }

    @Test
    public void test_getTotalPriceOfAllProducts_there_is_no_discount() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // empty products
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 150.0, new Category("food"));

        shoppingCartService.addItem(apple, 3);
        shoppingCartService.addItem(almonds, 3);

        Double expectedPrice = (100.0 * 3) + (150.0 * 3);
        assertEquals(expectedPrice, shoppingCartService.getTotalPriceOfProducts());
    }


    @Test
    public void test_apply_campaign_same_category_discount_to_cart() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // init empty products
        ShoppingCartServiceImpl shoppingCartServiceMock = mock(ShoppingCartServiceImpl.class);
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 150.0, new Category("food"));
        Product orange = new Product("Oranges", 50.0, new Category("food"));

        shoppingCartService.addItem(apple, 3);
        shoppingCartService.addItem(almonds, 3);
        shoppingCartService.addItem(orange, 1);

        Campaign campaign1 = new Campaign(new Category("food"), 20.0, 3, DiscountType.RATE);
        //Campaign campaign2 = new Campaign(new Category("food"), 50.0, 5, DiscountType.RATE);
        // Campaign campaign3 = new Campaign(new Category("food"), 5, 10, DiscountType.AMOUNT);

        // shoppingCartService.addingCampaignsToCart(Arrays.asList(campaign1, campaign2, campaign3));
        shoppingCartService.applyDiscounts(Arrays.asList(campaign1));
        Double totalPriceOfProductsWithoutDiscount = shoppingCartService.getTotalPriceOfProducts();
        assertEquals(totalPriceOfProductsWithoutDiscount, 800);


        Double expectedDiscountPrice = 160.0;
        assertEquals(expectedDiscountPrice, shoppingCartService.getCampaignDiscount());

        // Double expectedDiscountPrice =
        // Double campaignDiscount = shoppingCartService.getCampaignDiscount();

        //Double expectedPrice = (100.0 * 3) + (150.0 * 3);
        //assertEquals(expectedPrice, shoppingCartService.getTotalPriceOfProducts());
    }

    @Test
    public void applyDiscount_2DifferentCampaignButSameCategory() {
        ShoppingCartServiceImpl shoppingCartService = new ShoppingCartServiceImpl(); // init empty products
        ShoppingCartServiceImpl shoppingCartServiceMock = mock(ShoppingCartServiceImpl.class);
        Product apple = new Product("Apple", 100.0, new Category("food"));
        Product almonds = new Product("Almonds", 150.0, new Category("food"));
        Product orange = new Product("Oranges", 50.0, new Category("food"));

        shoppingCartService.addItem(apple, 3);
        shoppingCartService.addItem(almonds, 3);
        shoppingCartService.addItem(orange, 1);

        Campaign campaign1 = new Campaign(new Category("food"), 20.0, 3, DiscountType.RATE);
        Campaign campaign2 = new Campaign(new Category("food"), .0, 5, DiscountType.RATE);
        //Campaign campaign2 = new Campaign(new Category("food"), 50.0, 5, DiscountType.RATE);
        // Campaign campaign3 = new Campaign(new Category("food"), 5, 10, DiscountType.AMOUNT);

        // shoppingCartService.addingCampaignsToCart(Arrays.asList(campaign1, campaign2, campaign3));
        shoppingCartService.applyDiscounts(Arrays.asList(campaign1));
        Double totalPriceOfProductsWithoutDiscount = shoppingCartService.getTotalPriceOfProducts();
        assertEquals(totalPriceOfProductsWithoutDiscount, 800);


        Double expectedDiscountPrice = 160.0;
        assertEquals(expectedDiscountPrice, shoppingCartService.getCampaignDiscount());

        // Double expectedDiscountPrice =
        // Double campaignDiscount = shoppingCartService.getCampaignDiscount();

        //Double expectedPrice = (100.0 * 3) + (150.0 * 3);
        //assertEquals(expectedPrice, shoppingCartService.getTotalPriceOfProducts());
    }

    // GetCampaignDiscount test starting
    @Test
    public void getCampaignDiscount_twoCampaignsOneProductOnCategory_quantity_less_than_CampaignsNotShouldApplied()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int quantity = 2;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(category, 20, 3 , DiscountType.RATE);

        cart.applyDiscounts(c1, c2);

        Double expected = p1.getPrice() * quantity * 0;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    // ### GET CAMPAIGN DISCOUNT TEST ###
    @Test
    public void getCampaignDiscount_twoCampaignsOneProductOnCategory_quantity_equal_Campaign1ShouldApplied()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int quantity = 5;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(category, 20, 3 , DiscountType.RATE);

        cart.applyDiscounts(c1, c2);

        Double expected = p1.getPrice() * quantity * 0.50;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_twoCampaignsOneProductOnCategory_quantity_equal_Campaign2ShouldApplied()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int quantity = 3;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(category, 20, 3 , DiscountType.RATE);

        cart.applyDiscounts(c1, c2);

        Double expected = p1.getPrice() * quantity * 0.20;
        assertEquals(expected, cart.getCampaignDiscount());
    }


    @Test
    public void getCampaignDiscount_twoCampaignsOneProductOnCategory_inTheMiddleValue_Campaign2ShouldApplied()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int quantity = 4;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(category, 20, 3 , DiscountType.RATE);

        cart.applyDiscounts(c1, c2);

        Double expected = p1.getPrice() * quantity * 0.20;
        assertEquals(expected, cart.getCampaignDiscount());
    }


    @Test
    public void getCampaignDiscount_twoCampaignsOneProductOnCategory_greater_Campaign1ShouldApplied()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int quantity = 6;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(category, 20, 3 , DiscountType.RATE);

        cart.applyDiscounts(c1, c2);

        Double expected = p1.getPrice() * quantity * 0.50;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_CampaignOneProductWithAmountOnCategory_lessThanMinumum_ReturnsZero()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        int fixedDiscountAmount = 5;
        int quantity = 4;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, fixedDiscountAmount, 5, DiscountType.AMOUNT);
        cart.applyDiscounts(c1);

        Double expected = 0.0;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_CampaignOneProductWithAmountOnCategory_equal_ReturnsFixedDiscountAmount()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Double fixedDiscountAmount = 5.0;
        int quantity = 5;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, fixedDiscountAmount, 5, DiscountType.AMOUNT);
        cart.applyDiscounts(c1);

        Double expected = fixedDiscountAmount;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_CampaignOneProductWithAmountOnCategory_greater_returnsFixedDiscountAmount()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Double fixedDiscountAmount = 5.0;
        int quantity = 6;

        cart.addItem(p1, quantity);

        Campaign c1 = new Campaign(category, fixedDiscountAmount, 5, DiscountType.AMOUNT);
        cart.applyDiscounts(c1);

        Double expected = fixedDiscountAmount;
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_combinedTwoDifferentCategoryWith2Product_MinimumAmountEqualBounded_ApplyingTwoCampaignToCart()
    {
        Category fruitCategory = new Category("Fruit");
        Category vegatablesCategory = new Category("Vegatables");

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product parrot = new Product("Parrot", 50.0, vegatablesCategory);

        cart.addItem(apple, 5);
        cart.addItem(parrot, 3);

        Campaign c1 = new Campaign(fruitCategory, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(vegatablesCategory, 5, 3 , DiscountType.AMOUNT);

        cart.applyDiscounts(c1, c2);

        Double totalPrice = apple.getPrice() * 5 + parrot.getPrice() * 3;
        Double vegatablesDiscountExcepted = 5.0;
        Double fruitDiscountExcepted = totalPrice * 0.50;
        Double totalExpected = fruitDiscountExcepted + vegatablesDiscountExcepted;
        assertEquals(totalExpected, cart.getCampaignDiscount());
    }

    @Test
    public void getCampaignDiscount_combinedTwoDifferentCategoryWith2Product_Greater_ApplyingTwoCampaignToCart()
    {
        Category fruitCategory = new Category("Fruit");
        Category vegatablesCategory = new Category("Vegatables");

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product parrot = new Product("Parrot", 50.0, vegatablesCategory);

        int quantityOfAppleProduct = 10;
        cart.addItem(apple, quantityOfAppleProduct);
        cart.addItem(parrot, 3);

        Campaign c1 = new Campaign(fruitCategory, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(vegatablesCategory, 5, 3 , DiscountType.AMOUNT);

        cart.applyDiscounts(c1, c2);

        Double totalPrice = apple.getPrice() * quantityOfAppleProduct + parrot.getPrice() * 3;
        Double vegatablesDiscountExcepted = 5.0;
        Double fruitDiscountExcepted = totalPrice * 0.50;
        Double totalExpected = fruitDiscountExcepted + vegatablesDiscountExcepted;
        assertEquals(totalExpected, cart.getCampaignDiscount());
    }


    @Test
    public void getCampaignDiscount_combinedTwoDifferentCategoryWith2Product_Min_ApplyingTwoCampaignToCart()
    {
        Category fruitCategory = new Category("Fruit");
        Category vegatablesCategory = new Category("Vegatables");

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product parrot = new Product("Parrot", 50.0, vegatablesCategory);

        int quantityOfAppleProduct = 2;
        cart.addItem(apple, quantityOfAppleProduct);
        cart.addItem(parrot, 3);

        Campaign c1 = new Campaign(fruitCategory, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(vegatablesCategory, 5, 3 , DiscountType.AMOUNT);

        cart.applyDiscounts(c1, c2);

        Double totalPrice = apple.getPrice() * quantityOfAppleProduct + parrot.getPrice() * 3;
        Double vegatablesDiscountExcepted = 5.0;
        Double fruitDiscountExcepted = totalPrice * 0;
        Double totalExpected = fruitDiscountExcepted + vegatablesDiscountExcepted;
        assertEquals(totalExpected, cart.getCampaignDiscount());
    }
    @Test
    public void getCampaignDiscount_negativeQuantityValues_returnsZero()
    {
        Category fruitCategory = new Category("Fruit");
        Category vegatablesCategory = new Category("Vegatables");

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product parrot = new Product("Parrot", 50.0, vegatablesCategory);

        int quantityOfAppleProduct = -1;
        cart.addItem(apple, quantityOfAppleProduct);
        cart.addItem(parrot, -1);

        Campaign c1 = new Campaign(fruitCategory, 50, 5, DiscountType.RATE);
        Campaign c2 = new Campaign(vegatablesCategory, 5, 3 , DiscountType.AMOUNT);

        cart.applyDiscounts(c1, c2);

        Double totalPrice = apple.getPrice() * quantityOfAppleProduct + parrot.getPrice() * 3;
        Double vegatablesDiscountExcepted = 0.0;
        Double fruitDiscountExcepted = totalPrice * 0;
        Double totalExpected = fruitDiscountExcepted + vegatablesDiscountExcepted;
        assertEquals(totalExpected, cart.getCampaignDiscount());
    }
    // #### GET CAMPAIGN DISCOUNT END ###


    // ### COUPONS TEST CASES START POINT ###
    // Info: Apply one coupon per cart.
    @Test
    public void ApplyCouponToCart_IfCouponIsNull_ShouldThrowException() {
        Product one_product_apple = new Product("Apple", 100.0, new Category("Fruit-Category"));
        Coupon one_coupon = null;

        cart.addItem(one_product_apple, 1);

        Assertions.assertThrows(IllegalStateException.class, () -> cart.applyCoupon(one_coupon));
    }

    @Test
    public void ApplyCouponToCart_IfThereIsNoProductInCart_ShouldThrowExceptionNotApplicableThisCoupon() {
        Coupon one_coupon = new Coupon(200, 33.0, DiscountType.AMOUNT);

        Assertions.assertThrows(IllegalStateException.class, () -> cart.applyCoupon(one_coupon));
    }

    @Test
    public void ApplyCouponToCart_CouponMinumumAmountGreaterThanTotalCartIsNotApplicable_ShouldThrowException() {
        Product one_product_apple = new Product("Apple", 100.0, new Category("Fruit-Category"));
        Coupon one_coupon = new Coupon(200, 33.0, DiscountType.AMOUNT);

        cart.addItem(one_product_apple, 1);

        Assertions.assertThrows(IllegalStateException.class, () -> cart.applyCoupon(one_coupon));
    }

    @Test
    public void ApplyCouponToCart_oneProductOneCouponAppliedWithRate_ShouldReturnRateDiscountAmountOfTotalPrice() {
        Product apple_product = new Product("Apple", 100.0, new Category("Fruit-Category"));
        Coupon coupon_min_100_rate_10 = new Coupon(100, 10.0, DiscountType.RATE);

        log.info("Applying coupon: {} to cart", coupon_min_100_rate_10);
        cart.addItem(apple_product, 10);
        cart.applyCoupon(coupon_min_100_rate_10);

        Double expected = 100.0;
        assertEquals(expected, cart.getCouponDiscount());
    }

    @Test
    public void ApplyCouponToCart_oneProductOneCouponAppliedWithAmount_ShouldReturnDiscountAmount() {
        Product apple_product = new Product("Apple", 100.0, new Category("Fruit-Category"));
        Coupon coupon_min_100_rate_10 = new Coupon(100, 33.0, DiscountType.AMOUNT);

        log.info("Applying coupon: {} to cart", coupon_min_100_rate_10);
        cart.addItem(apple_product, 10);
        cart.applyCoupon(coupon_min_100_rate_10);

        Double expected = 33.0;
        assertEquals(expected, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_NoCouponApplied_ReturnsZero()
    {
        assertEquals(0, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponOneProductCartTotalGreaterThanMinimumAmount_ReturnsTen()
    {
        Product p1 = new Product("Apple", 100.0, new Category("Fruit"));
        cart.addItem(p1, 3);

        Coupon coupon = new Coupon(2, 10.0, DiscountType.AMOUNT);
        cart.applyCoupon(coupon);

        assertEquals(10.0, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponOneProductTotalPriceMinumumThanMinumumAmount_ThrowExceptionAndReturnsZero()
    {
        Product p1 = new Product("Apple", 100.0, new Category("Fruit"));
        cart.addItem(p1, 3);

        Coupon coupon = new Coupon(1000, 5.0, DiscountType.AMOUNT);
        assertThrows(IllegalStateException.class, () -> cart.applyCoupon(coupon));

        assertEquals(0, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponOneProductLessThanMinimumAmount_NotApplicableCouponShouldReturnsZero()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        cart.addItem(p1, 3);

        Coupon coupon = new Coupon(400, 10.0, DiscountType.AMOUNT);
        assertThrows(IllegalStateException.class, () -> cart.applyCoupon(coupon));
        assertEquals(0, cart.getCouponDiscount());
    }


    @Test
    public void GetCouponDiscount_CouponTwoProductGreaterThanMinimumAmount_ReturnsTwoHundred()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Product p2 = new Product("Orange", 50.0, category);

        cart.addItem(p1, 4);
        cart.addItem(p2, 2);


        Coupon coupon = new Coupon(400, 200.0, DiscountType.AMOUNT);
        cart.applyCoupon(coupon);
        assertEquals(200.0, cart.getCouponDiscount());
    }


    @Test
    public void GetCouponDiscount_CouponTwoProductLessThanMinimumAmount_ReturnsZero()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Product p2 = new Product("Orange", 50.0, category);

        cart.addItem(p1, 3);
        cart.addItem(p2, 2);

        Coupon coupon = new Coupon(500, 100.0, DiscountType.AMOUNT);
        assertThrows(IllegalStateException.class, () -> cart.applyCoupon(coupon));
        assertEquals(0, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CampaingWithRateOneProductOnCategoryGreaterThanMinimumAmount_ReturnsTwoHundred()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);

        cart.addItem(p1, 4);

        double expected = cart.getTotalPriceOfProducts() * 0.5;

        Coupon coupon = new Coupon(200, 50.0, DiscountType.RATE);
        cart.applyCoupon(coupon);
        assertEquals(200, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponWithRateOneProductLessThanMinimumAmount_ReturnsZero()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        cart.addItem(p1, 3);

        Coupon coupon = new Coupon(500, 50.0, DiscountType.RATE);
        assertThrows(IllegalStateException.class, () -> cart.applyCoupon(coupon));
        assertEquals(0, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponWithTwoProductGreaterThanMinimumAmount_ReturnsForty()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Product p2 = new Product("Orange", 50.00, category);

        cart.addItem(p1, 3);
        cart.addItem(p2, 2);

        double expected = cart.getTotalPriceOfProducts() * 0.1;

        Coupon coupon = new Coupon(300, 10.0, DiscountType.RATE);
        cart.applyCoupon(coupon);
        assertEquals(expected, cart.getCouponDiscount());
    }

    @Test
    public void GetCouponDiscount_CouponWithRateTwoProductLessThanMinimumAmount_ReturnsZero()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        Product p2 = new Product("Orange", 50.0, category);

        cart.addItem(p1, 3);
        cart.addItem(p2, 2);

        Coupon coupon = new Coupon(450, 10.0, DiscountType.RATE);
        assertThrows(IllegalStateException.class, () -> cart.applyCoupon(coupon));
        assertEquals(0, cart.getCouponDiscount());
    }

    // >>> GetTotalAmountAfterDiscounts Testing <<<
    @Test
    public void GetTotalAmountAfterDiscounts_NoProductInEmptyCart_ReturnsZero()
    {
        assertEquals(0, cart.getTotalAmountAfterDiscounts());
    }

     @Test
    public void GetTotalAmountAfterDiscounts_OneProductWithoutAnyDiscount_ReturnsTotalPriceOfProducts()
    {
        Product p1 = new Product("Apple", 50.0, new Category("Fruit"));
        cart.addItem(p1, 5);

        assertEquals(cart.getTotalPriceOfProducts(), cart.getTotalAmountAfterDiscounts());
    }

    @Test
    public void GetTotalAmountAfterDiscounts_OneProductOneCampaignNoCoupon_ReturnsTotalMinusCampaignDiscount()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 50.0, category);
        cart.addItem(p1, 5);

        Campaign c1 = new Campaign(category, 4,  5, DiscountType.AMOUNT);
        cart.applyDiscounts(c1);

        assertEquals(cart.getTotalPriceOfProducts() - cart.getCampaignDiscount(), cart.getTotalAmountAfterDiscounts());
    }

    @Test
    public void GetTotalAmountAfterDiscounts_OneProductOneCampaignWithCoupon_ReturnsTotalAmountMinusCampaignDiscountAndCouponDiscount()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        cart.addItem(p1, 5);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.AMOUNT);
        cart.applyDiscounts(c1);

        Coupon coupon = new Coupon(250, 100.0, DiscountType.AMOUNT);
        cart.applyCoupon(coupon);

        double totalAmountAfterCampaign = cart.getTotalPriceOfProducts() - cart.getCampaignDiscount();
        double expected = totalAmountAfterCampaign - 100.0;
        assertEquals(expected, cart.getTotalAmountAfterDiscounts());

    }

    @Test
    public void GetTotalAmountAfterDiscounts_OneProductOneCampaignWithCouponWithRate_ReturnsTotalAmountMinusCampaignDiscountAndCouponDiscount()
    {
        Category category = new Category("Fruit");
        Product p1 = new Product("Apple", 100.0, category);
        cart.addItem(p1, 5);

        Campaign c1 = new Campaign(category, 50, 5, DiscountType.RATE);
        cart.applyDiscounts(c1);

        Coupon coupon = new Coupon(250, 10.0, DiscountType.RATE);
        cart.applyCoupon(coupon);

        double totalAmountAfterCampaign = cart.getTotalPriceOfProducts() - cart.getCampaignDiscount();
        double expected = totalAmountAfterCampaign - (totalAmountAfterCampaign * 0.1);
        assertEquals(expected, cart.getTotalAmountAfterDiscounts());

    }
}
