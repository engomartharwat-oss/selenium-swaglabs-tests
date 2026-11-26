package com.omartharwat.saucedemo.tests;

import com.omartharwat.framework.config.ConfigReader;
import com.omartharwat.framework.utils.CsvReader;
import com.omartharwat.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return CsvReader.read("testdata/invalid-logins.csv");
    }

    @DataProvider(name = "allowedUsers")
    public Object[][] allowedUsers() {
        return new Object[][]{
                {ConfigReader.get("user.standard")},
                {ConfigReader.get("user.problem")},
                {ConfigReader.get("user.performance")}
        };
    }

    @Test(description = "The login form shows username, password and the login button")
    public void loginFormIsDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormShown());
    }

    @Test(description = "A valid user reaches the products page")
    public void standardUserCanLogIn() {
        InventoryPage inventory = loginAsStandardUser();
        Assert.assertTrue(inventory.isLoaded(), "Products page should be open");
        Assert.assertTrue(inventory.currentUrl().contains("inventory.html"));
    }

    @Test(dataProvider = "allowedUsers", description = "Different valid users can all log in")
    public void allowedUsersCanLogIn(String username) {
        InventoryPage inventory = loginPage.loginAs(username, ConfigReader.get("password"));
        Assert.assertTrue(inventory.isLoaded(), username + " should reach the products page");
    }

    @Test(description = "The locked out user is blocked with a clear message")
    public void lockedOutUserSeesError() {
        loginPage.submitExpectingFailure(ConfigReader.get("user.locked"), ConfigReader.get("password"));
        Assert.assertEquals(loginPage.errorText(),
                "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(dataProvider = "invalidLogins", description = "Wrong or missing credentials show the right error (data from CSV)")
    public void invalidLoginShowsError(String description, String username, String password, String expectedError) {
        loginPage.submitExpectingFailure(username, password);
        Assert.assertTrue(loginPage.isErrorShown(), "Error should be visible for: " + description);
        Assert.assertEquals(loginPage.errorText(), expectedError);
    }

    @Test(description = "The error banner can be closed with the X button")
    public void errorMessageCanBeDismissed() {
        loginPage.submitExpectingFailure("", "");
        Assert.assertTrue(loginPage.isErrorShown());
        loginPage.dismissError();
        Assert.assertFalse(loginPage.isErrorShown(), "Error should disappear after closing it");
    }
}
