package com.omartharwat.saucedemo.pages;

import com.omartharwat.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/** Checkout step two: order summary with item total, tax and total. */
public class CheckoutOverviewPage extends BasePage {

    private final By title = By.cssSelector(".title");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By subtotal = By.cssSelector(".summary_subtotal_label");
    private final By tax = By.cssSelector(".summary_tax_label");
    private final By total = By.cssSelector(".summary_total_label");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return textOf(title);
    }

    public List<String> itemNames() {
        return textsOf(itemNames);
    }

    public List<Double> itemPrices() {
        return textsOf(itemPrices).stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .collect(Collectors.toList());
    }

    public double subtotalAmount() {
        return amountAfterDollar(textOf(subtotal));
    }

    public double taxAmount() {
        return amountAfterDollar(textOf(tax));
    }

    public double totalAmount() {
        return amountAfterDollar(textOf(total));
    }

    private static double amountAfterDollar(String label) {
        return Double.parseDouble(label.substring(label.indexOf('$') + 1));
    }

    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }
}
