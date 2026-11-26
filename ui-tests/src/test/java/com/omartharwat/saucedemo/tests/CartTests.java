package com.omartharwat.saucedemo.tests;

import com.omartharwat.saucedemo.pages.CartPage;
import com.omartharwat.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CartTests extends BaseTest {

    @Test(description = "Adding one item shows badge 1 and turns the button into Remove")
    public void addSingleItemShowsBadge() {
        InventoryPage inventory = loginAsStandardUser().addToCart(BACKPACK);
        Assert.assertEquals(inventory.cartBadgeCount(), 1);
        Assert.assertTrue(inventory.isRemoveButtonShown(BACKPACK));
    }

    @Test(description = "Adding three items shows badge 3")
    public void addMultipleItemsUpdatesBadge() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart(BACKPACK).addToCart(BIKE_LIGHT).addToCart(ONESIE);
        Assert.assertEquals(inventory.cartBadgeCount(), 3);
    }

    @Test(description = "Removing the item from the products page clears the badge")
    public void removeItemFromInventoryClearsBadge() {
        InventoryPage inventory = loginAsStandardUser().addToCart(BACKPACK).removeFromCart(BACKPACK);
        Assert.assertEquals(inventory.cartBadgeCount(), 0);
        Assert.assertTrue(inventory.isAddButtonShown(BACKPACK));
    }

    @Test(description = "The cart page lists exactly what was added")
    public void cartPageListsAddedItems() {
        CartPage cart = loginAsStandardUser().addToCart(BACKPACK).addToCart(BOLT_SHIRT).openCart();
        Assert.assertTrue(cart.isLoaded());
        Assert.assertEquals(cart.itemNames(), Arrays.asList(BACKPACK, BOLT_SHIRT));
    }

    @Test(description = "An empty cart shows no items")
    public void emptyCartHasNoItems() {
        CartPage cart = loginAsStandardUser().openCart();
        Assert.assertEquals(cart.itemCount(), 0);
    }

    @Test(description = "An item can be removed on the cart page")
    public void removeItemFromCartPage() {
        CartPage cart = loginAsStandardUser().addToCart(BACKPACK).addToCart(ONESIE).openCart().remove(BACKPACK);
        Assert.assertEquals(cart.itemNames(), Arrays.asList(ONESIE));
    }

    @Test(description = "Continue Shopping goes back to the products page")
    public void continueShoppingReturnsToInventory() {
        InventoryPage inventory = loginAsStandardUser().openCart().continueShopping();
        Assert.assertTrue(inventory.isLoaded());
    }

    @Test(description = "The cart keeps its items after a page refresh")
    public void cartSurvivesRefresh() {
        InventoryPage inventory = loginAsStandardUser().addToCart(FLEECE_JACKET);
        inventory.refresh();
        Assert.assertEquals(inventory.cartBadgeCount(), 1);
    }

    @Test(description = "Adding an item from the product details page updates the badge")
    public void addFromProductDetailsPage() {
        InventoryPage inventory = loginAsStandardUser().openProduct(RED_SHIRT).addToCart().backToProducts();
        Assert.assertEquals(inventory.cartBadgeCount(), 1);
        Assert.assertTrue(inventory.isRemoveButtonShown(RED_SHIRT));
    }
}
