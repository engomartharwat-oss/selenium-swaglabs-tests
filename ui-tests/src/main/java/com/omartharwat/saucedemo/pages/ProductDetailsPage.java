package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By name = By.cssSelector("[data-test='inventory-item-name']");
    private final By price = By.cssSelector("[data-test='inventory-item-price']");
    private final By description = By.cssSelector("[data-test='inventory-item-desc']");
    private final By backButton = By.id("back-to-products");
    private final By addButton = By.cssSelector("button[id^='add-to-cart']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String productName() {
        return textOf(name);
    }

    public String productPrice() {
        return textOf(price);
    }

    public String productDescription() {
        return textOf(description);
    }

    public ProductDetailsPage addToCart() {
        click(addButton);
        return this;
    }

    public InventoryPage backToProducts() {
        click(backButton);
        return new InventoryPage(driver);
    }
}
