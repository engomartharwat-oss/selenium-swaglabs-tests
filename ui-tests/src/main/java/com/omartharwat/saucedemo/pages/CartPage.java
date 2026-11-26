package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import com.omartharwat.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By continueShopping = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilDisplayed(title) && textOf(title).equals("Your Cart");
    }

    public List<String> itemNames() {
        WaitUtils.visible(driver, title);
        return textsOf(itemNames);
    }

    public int itemCount() {
        WaitUtils.visible(driver, title);
        return driver.findElements(cartItems).size();
    }

    public CartPage remove(String productName) {
        click(By.id("remove-" + productName.toLowerCase().replace(" ", "-")));
        return this;
    }

    public InventoryPage continueShopping() {
        click(continueShopping);
        return new InventoryPage(driver);
    }

    public CheckoutInfoPage checkout() {
        click(checkoutButton);
        return new CheckoutInfoPage(driver);
    }
}
