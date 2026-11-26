package com.omartharwat.framework.utils;

import com.omartharwat.framework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Small helpers around explicit waits. The framework never uses Thread.sleep. */
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait waiter(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    public static WebElement visible(WebDriver driver, By locator) {
        return waiter(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(WebDriver driver, By locator) {
        return waiter(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean invisible(WebDriver driver, By locator) {
        return waiter(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean urlContains(WebDriver driver, String fragment) {
        return waiter(driver).until(ExpectedConditions.urlContains(fragment));
    }
}
