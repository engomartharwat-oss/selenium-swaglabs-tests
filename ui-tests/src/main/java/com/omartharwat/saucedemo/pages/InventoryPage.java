package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import com.omartharwat.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By items = By.cssSelector(".inventory_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilDisplayed(title) && textOf(title).equals("Products");
    }

    public int productCount() {
        WaitUtils.visible(driver, items);
        return driver.findElements(items).size();
    }

    public List<String> productNames() {
        WaitUtils.visible(driver, itemNames);
        return textsOf(itemNames);
    }

    public List<Double> productPrices() {
        WaitUtils.visible(driver, itemPrices);
        return textsOf(itemPrices).stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .collect(Collectors.toList());
    }

    /** @param visibleText one of: "Name (A to Z)", "Name (Z to A)", "Price (low to high)", "Price (high to low)" */
    public InventoryPage sortBy(String visibleText) {
        WebElement dropdown = WaitUtils.visible(driver, sortDropdown);
        new Select(dropdown).selectByVisibleText(visibleText);
        return this;
    }

    private By addButtonFor(String productName) {
        return By.id("add-to-cart-" + slug(productName));
    }

    private By removeButtonFor(String productName) {
        return By.id("remove-" + slug(productName));
    }

    private static String slug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }

    public InventoryPage addToCart(String productName) {
        click(addButtonFor(productName));
        return this;
    }

    public InventoryPage removeFromCart(String productName) {
        click(removeButtonFor(productName));
        return this;
    }

    public boolean isRemoveButtonShown(String productName) {
        return isDisplayed(removeButtonFor(productName));
    }

    public boolean isAddButtonShown(String productName) {
        return isDisplayed(addButtonFor(productName));
    }

    public int cartBadgeCount() {
        List<WebElement> badge = driver.findElements(cartBadge);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public ProductDetailsPage openProduct(String productName) {
        click(By.xpath("//div[@class='inventory_item_name ' and text()='" + productName + "']"));
        return new ProductDetailsPage(driver);
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public MenuComponent menu() {
        return new MenuComponent(driver);
    }
}
