import pytest
from pages.login_page import LoginPage
from pages.inventory_page import InventoryPage
from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage


def test_full_purchase_flow(driver):
    """End to end: log in, add a product, check out, see the confirmation."""
    LoginPage(driver).open().login("standard_user", "secret_sauce")
    inventory = InventoryPage(driver)
    inventory.add_item_to_cart("sauce-labs-backpack")
    inventory.open_cart()

    CartPage(driver).checkout()

    checkout = CheckoutPage(driver)
    checkout.fill_customer_info("Omar", "Tharwat", "11511")
    checkout.finish()

    assert "Thank you for your order" in checkout.confirmation_text()
