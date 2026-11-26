package com.omartharwat.saucedemo.tests;

import com.omartharwat.framework.config.ConfigReader;
import com.omartharwat.framework.driver.DriverFactory;
import com.omartharwat.framework.driver.DriverManager;
import com.omartharwat.saucedemo.pages.InventoryPage;
import com.omartharwat.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** Every test gets a fresh browser, and it is closed after the test. */
public abstract class BaseTest {

    protected static final String BACKPACK = "Sauce Labs Backpack";
    protected static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    protected static final String BOLT_SHIRT = "Sauce Labs Bolt T-Shirt";
    protected static final String FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    protected static final String ONESIE = "Sauce Labs Onesie";
    protected static final String RED_SHIRT = "Test.allTheThings() T-Shirt (Red)";

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
        loginPage = new LoginPage(driver).open(ConfigReader.get("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected InventoryPage loginAsStandardUser() {
        return loginPage.loginAs(ConfigReader.get("user.standard"), ConfigReader.get("password"));
    }
}
