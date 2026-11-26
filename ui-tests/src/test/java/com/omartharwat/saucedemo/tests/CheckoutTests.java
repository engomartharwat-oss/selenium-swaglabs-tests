package com.omartharwat.saucedemo.tests;

import com.omartharwat.framework.utils.CsvReader;
import com.omartharwat.saucedemo.pages.CartPage;
import com.omartharwat.saucedemo.pages.CheckoutCompletePage;
import com.omartharwat.saucedemo.pages.CheckoutInfoPage;
import com.omartharwat.saucedemo.pages.CheckoutOverviewPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CheckoutTests extends BaseTest {

    @DataProvider(name = "checkoutValidation")
    public Object[][] checkoutValidation() {
        return CsvReader.read("testdata/checkout-validation.csv");
    }

    private CheckoutInfoPage goToCheckoutWithBackpack() {
        return loginAsStandardUser().addToCart(BACKPACK).openCart().checkout();
    }

    @Test(description = "Full purchase from login to the thank you page")
    public void fullPurchaseEndToEnd() {
        CheckoutCompletePage complete = goToCheckoutWithBackpack()
                .fillForm("Omar", "Tharwat", "11511")
                .continueToOverview()
                .finish();
        Assert.assertEquals(complete.title(), "Checkout: Complete!");
        Assert.assertEquals(complete.confirmationHeader(), "Thank you for your order!");
    }

    @Test(description = "After a purchase the cart is empty again")
    public void cartIsEmptyAfterPurchase() {
        CheckoutCompletePage complete = goToCheckoutWithBackpack()
                .fillForm("Omar", "Tharwat", "11511")
                .continueToOverview()
                .finish();
        Assert.assertEquals(complete.backHome().cartBadgeCount(), 0);
    }

    @Test(dataProvider = "checkoutValidation", description = "Missing form fields show the right error (data from CSV)")
    public void checkoutFormValidation(String description, String first, String last, String zip, String expectedError) {
        CheckoutInfoPage form = goToCheckoutWithBackpack().fillForm(first, last, zip).continueExpectingError();
        Assert.assertEquals(form.errorText(), expectedError, description);
    }

    @Test(description = "The overview lists the items that are in the cart")
    public void overviewListsCartItems() {
        CheckoutOverviewPage overview = loginAsStandardUser()
                .addToCart(BACKPACK).addToCart(BIKE_LIGHT)
                .openCart().checkout()
                .fillForm("Omar", "Tharwat", "11511")
                .continueToOverview();
        Assert.assertEquals(overview.title(), "Checkout: Overview");
        Assert.assertEquals(overview.itemNames(), Arrays.asList(BACKPACK, BIKE_LIGHT));
    }

    @Test(description = "Item total is the sum of item prices and total is item total plus tax")
    public void overviewTotalsAreCorrect() {
        CheckoutOverviewPage overview = loginAsStandardUser()
                .addToCart(BACKPACK).addToCart(BIKE_LIGHT).addToCart(BOLT_SHIRT)
                .openCart().checkout()
                .fillForm("Omar", "Tharwat", "11511")
                .continueToOverview();
        double sumOfItems = overview.itemPrices().stream().mapToDouble(Double::doubleValue).sum();
        Assert.assertEquals(overview.subtotalAmount(), sumOfItems, 0.001, "Item total");
        Assert.assertEquals(overview.totalAmount(), overview.subtotalAmount() + overview.taxAmount(), 0.011, "Total");
    }

    @Test(description = "Cancel on the information form returns to the cart")
    public void cancelCheckoutReturnsToCart() {
        CartPage cart = goToCheckoutWithBackpack().cancel();
        Assert.assertTrue(cart.isLoaded());
        Assert.assertEquals(cart.itemNames(), Arrays.asList(BACKPACK));
    }
}
