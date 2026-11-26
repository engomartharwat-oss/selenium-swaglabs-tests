package com.omartharwat.saucedemo.tests;

import com.omartharwat.saucedemo.pages.InventoryPage;
import com.omartharwat.saucedemo.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryTests extends BaseTest {

    @DataProvider(name = "productsWithPrices")
    public Object[][] productsWithPrices() {
        return new Object[][]{
                {BACKPACK, "$29.99"},
                {BIKE_LIGHT, "$9.99"},
                {BOLT_SHIRT, "$15.99"}
        };
    }

    @Test(description = "The shop lists six products")
    public void inventoryShowsSixProducts() {
        InventoryPage inventory = loginAsStandardUser();
        Assert.assertEquals(inventory.productCount(), 6);
    }

    @Test(description = "Default order is Name (A to Z)")
    public void defaultSortIsNameAscending() {
        List<String> names = loginAsStandardUser().productNames();
        List<String> expected = new ArrayList<>(names);
        Collections.sort(expected);
        Assert.assertEquals(names, expected);
    }

    @Test(description = "Sort by name Z to A")
    public void sortByNameDescending() {
        List<String> names = loginAsStandardUser().sortBy("Name (Z to A)").productNames();
        List<String> expected = new ArrayList<>(names);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(names, expected);
    }

    @Test(description = "Sort by price low to high")
    public void sortByPriceAscending() {
        List<Double> prices = loginAsStandardUser().sortBy("Price (low to high)").productPrices();
        List<Double> expected = new ArrayList<>(prices);
        Collections.sort(expected);
        Assert.assertEquals(prices, expected);
        Assert.assertEquals(prices.get(0), 7.99, 0.001, "Cheapest item is $7.99");
    }

    @Test(description = "Sort by price high to low")
    public void sortByPriceDescending() {
        List<Double> prices = loginAsStandardUser().sortBy("Price (high to low)").productPrices();
        List<Double> expected = new ArrayList<>(prices);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(prices, expected);
        Assert.assertEquals(prices.get(0), 49.99, 0.001, "Most expensive item is $49.99");
    }

    @Test(dataProvider = "productsWithPrices", description = "Product details page shows the right name and price")
    public void productDetailsPageShowsCorrectData(String name, String price) {
        ProductDetailsPage details = loginAsStandardUser().openProduct(name);
        Assert.assertEquals(details.productName(), name);
        Assert.assertEquals(details.productPrice(), price);
        Assert.assertFalse(details.productDescription().isEmpty(), "Description should not be empty");
    }

    @Test(description = "Back to products returns to the list")
    public void backToProductsFromDetails() {
        InventoryPage inventory = loginAsStandardUser().openProduct(BACKPACK).backToProducts();
        Assert.assertTrue(inventory.isLoaded());
        Assert.assertEquals(inventory.productCount(), 6);
    }
}
