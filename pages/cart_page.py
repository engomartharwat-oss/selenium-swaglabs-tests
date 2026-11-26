from selenium.webdriver.common.by import By
from .base_page import BasePage


class CartPage(BasePage):
    """The shopping cart page."""

    CART_ITEMS = (By.CSS_SELECTOR, ".cart_item")
    ITEM_NAMES = (By.CSS_SELECTOR, ".inventory_item_name")
    CHECKOUT_BUTTON = (By.ID, "checkout")

    def item_names(self):
        return [el.text for el in self.find_all(self.ITEM_NAMES)]

    def checkout(self):
        self.js_click(self.CHECKOUT_BUTTON)
        self.wait_for_url_to_contain("checkout-step-one")
