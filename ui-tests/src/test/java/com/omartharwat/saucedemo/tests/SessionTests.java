package com.omartharwat.saucedemo.tests;

import com.omartharwat.framework.config.ConfigReader;
import com.omartharwat.saucedemo.pages.InventoryPage;
import com.omartharwat.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SessionTests extends BaseTest {

    @Test(description = "Logout from the menu returns to the login page")
    public void logoutReturnsToLoginPage() {
        LoginPage login = loginAsStandardUser().menu().logout();
        Assert.assertTrue(login.isLoginFormShown());
    }

    @Test(description = "Opening the products URL without logging in is blocked")
    public void cannotOpenInventoryWithoutLogin() {
        driver.get(ConfigReader.get("base.url") + "inventory.html");
        Assert.assertTrue(loginPage.isErrorShown(), "An error should explain that login is required");
        Assert.assertTrue(loginPage.errorText().contains("You can only access '/inventory.html' when you are logged in"));
    }

    @Test(description = "After logout the Back button does not show the shop again")
    public void backButtonAfterLogoutDoesNotRestoreSession() {
        loginAsStandardUser().menu().logout();
        driver.navigate().back();
        Assert.assertTrue(loginPage.isLoginFormShown() || loginPage.isErrorShown());
    }

    @Test(description = "Reset App State from the menu empties the cart")
    public void resetAppStateEmptiesCart() {
        InventoryPage inventory = loginAsStandardUser().addToCart(BACKPACK).addToCart(ONESIE);
        Assert.assertEquals(inventory.cartBadgeCount(), 2);
        inventory.menu().resetAppState();
        inventory.refresh();
        Assert.assertEquals(inventory.cartBadgeCount(), 0);
    }

    @Test(description = "All Items in the menu goes back to the products page")
    public void menuAllItemsOpensProducts() {
        InventoryPage inventory = loginAsStandardUser().openCart().continueShopping();
        Assert.assertTrue(inventory.menu().openAllItems().isLoaded());
    }
}
