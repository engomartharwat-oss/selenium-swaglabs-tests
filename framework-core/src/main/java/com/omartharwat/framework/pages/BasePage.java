package com.omartharwat.framework.pages;

import com.omartharwat.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/** Common actions shared by every page object. Page classes extend this. */
public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(By locator) {
        WaitUtils.clickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement field = WaitUtils.visible(driver, locator);
        field.clear();
        if (text != null && !text.isEmpty()) {
            field.sendKeys(text);
        }
    }

    protected String textOf(By locator) {
        return WaitUtils.visible(driver, locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> found = driver.findElements(locator);
        return !found.isEmpty() && found.get(0).isDisplayed();
    }

    /** Waits up to the explicit wait time for the element to appear, and returns false instead of throwing. */
    protected boolean waitUntilDisplayed(By locator) {
        try {
            return WaitUtils.visible(driver, locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected List<String> textsOf(By locator) {
        return driver.findElements(locator).stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public void refresh() {
        driver.navigate().refresh();
    }
}
