package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import com.omartharwat.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The burger menu that appears on every page after login. */
public class MenuComponent extends BasePage {

    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetLink = By.id("reset_sidebar_link");
    private final By allItemsLink = By.id("inventory_sidebar_link");

    public MenuComponent(WebDriver driver) {
        super(driver);
    }

    private void open() {
        click(menuButton);
        WaitUtils.visible(driver, logoutLink);
    }

    public LoginPage logout() {
        open();
        click(logoutLink);
        return new LoginPage(driver);
    }

    public void resetAppState() {
        open();
        click(resetLink);
    }

    public InventoryPage openAllItems() {
        open();
        click(allItemsLink);
        return new InventoryPage(driver);
    }
}
