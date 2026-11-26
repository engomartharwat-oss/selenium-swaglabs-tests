package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By errorCloseButton = By.cssSelector(".error-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public LoginPage enterCredentials(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        enterCredentials(username, password);
        click(loginButton);
        return new InventoryPage(driver);
    }

    /** Use this when the login is expected to fail and we stay on the login page. */
    public LoginPage submitExpectingFailure(String username, String password) {
        enterCredentials(username, password);
        click(loginButton);
        return this;
    }

    public String errorText() {
        return textOf(errorMessage);
    }

    public boolean isErrorShown() {
        return isDisplayed(errorMessage);
    }

    public void dismissError() {
        click(errorCloseButton);
    }

    public boolean isLoginFormShown() {
        return isDisplayed(usernameField) && isDisplayed(passwordField) && isDisplayed(loginButton);
    }
}
