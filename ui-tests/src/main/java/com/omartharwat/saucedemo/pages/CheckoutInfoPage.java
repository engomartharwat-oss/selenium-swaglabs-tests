package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: the customer information form. */
public class CheckoutInfoPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By error = By.cssSelector("[data-test='error']");
    private final By title = By.cssSelector(".title");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return textOf(title);
    }

    public CheckoutInfoPage fillForm(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    /** Use this when the form is expected to show a validation error. */
    public CheckoutInfoPage continueExpectingError() {
        click(continueButton);
        return this;
    }

    public String errorText() {
        return textOf(error);
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver);
    }
}
