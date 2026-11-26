package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By header = By.cssSelector(".complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return textOf(title);
    }

    public String confirmationHeader() {
        return textOf(header);
    }

    public InventoryPage backHome() {
        click(backHomeButton);
        return new InventoryPage(driver);
    }
}
