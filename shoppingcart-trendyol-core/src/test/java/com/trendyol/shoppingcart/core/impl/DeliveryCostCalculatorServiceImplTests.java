package com.trendyol.shoppingcart.core.impl;

import com.trendyol.shoppingcart.api.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class DeliveryCostCalculatorServiceImplTests {
    private DeliveryCostCalculatorServiceImpl calculator;
    private ShoppingCartService cart;

    @BeforeEach
    public  void setUp()
    {
        cart = Mockito.mock(ShoppingCartService.class);
    }

    @Test
    public void constructor_Calculator()
    {
        Double costPerDelivery = 1.0, costPerProduct = 2.0, fixedCost = 2.99;
        calculator = new DeliveryCostCalculatorServiceImpl(costPerDelivery, costPerProduct, fixedCost);
        calculator = new DeliveryCostCalculatorServiceImpl(2.0, 2.0, 2.99);
    }

    @Test
    public void CalculateFor_NullShoppingCart_ReturnsException()
    {
        calculator = new DeliveryCostCalculatorServiceImpl(2.0, 2.0, 2.99);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFor(null));
    }

    @Test
    public void CalculateFor_CartWithoutProduct_ReturnsFixedCost()
    {
        calculator = new DeliveryCostCalculatorServiceImpl(2.0, 2.0, 2.99);

        Mockito.when(cart.getNumberOfProducts()).thenReturn(0);
        Mockito.when(cart.getNumberOfDeliveries()).thenReturn(0);
        assertEquals(2.99, calculator.calculateFor(cart));

    }
    @Test
    public void CalculateFor_CartWithTwoDeliveryTwoProduct_ReturnsValidCalculation()
    {
        calculator = new DeliveryCostCalculatorServiceImpl(2.0, 2.0, 2.99);

        Mockito.when(cart.getNumberOfProducts()).thenReturn(2);
        Mockito.when(cart.getNumberOfDeliveries()).thenReturn(2);
        assertEquals(10.99, calculator.calculateFor(cart));
    }
}
