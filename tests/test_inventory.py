import pytest
from pages.login_page import LoginPage
from pages.inventory_page import InventoryPage
from pages.cart_page import CartPage


@pytest.fixture
def inventory(driver):
    """Log in first and hand the test a ready products page."""
    LoginPage(driver).open().login("standard_user", "secret_sauce")
    return InventoryPage(driver)


def test_adding_a_product_updates_the_cart_badge(driver, inventory):
    """Adding one product shows 1 on the cart icon."""
    inventory.add_item_to_cart("sauce-labs-backpack")
    assert inventory.cart_count() == 1


def test_removing_a_product_clears_the_cart_badge(driver, inventory):
    """Removing the product takes the cart count back to zero."""
    inventory.add_item_to_cart("sauce-labs-backpack")
    inventory.remove_item_from_cart("sauce-labs-backpack")
    assert inventory.cart_count() == 0


def test_sorting_by_price_low_to_high(driver, inventory):
    """The sort dropdown really orders products from cheapest to most expensive."""
    inventory.sort_by("lohi")
    prices = inventory.listed_prices()
    assert prices == sorted(prices)


def test_cart_page_lists_the_added_product(driver, inventory):
    """The cart page shows the exact product that was added."""
    inventory.add_item_to_cart("sauce-labs-bike-light")
    inventory.open_cart()
    assert "Sauce Labs Bike Light" in CartPage(driver).item_names()


def test_user_can_log_out(driver, inventory):
    """Logging out returns the user to the login page."""
    inventory.logout()
    assert driver.current_url == "https://www.saucedemo.com/"
