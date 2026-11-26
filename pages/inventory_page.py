from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from .base_page import BasePage


class InventoryPage(BasePage):
    """The products list the user sees after logging in."""

    TITLE = (By.CSS_SELECTOR, ".title")
    CART_BADGE = (By.CSS_SELECTOR, ".shopping_cart_badge")
    CART_LINK = (By.CSS_SELECTOR, ".shopping_cart_link")
    SORT_DROPDOWN = (By.CSS_SELECTOR, ".product_sort_container")
    ITEM_PRICES = (By.CSS_SELECTOR, ".inventory_item_price")
    MENU_BUTTON = (By.ID, "react-burger-menu-btn")
    LOGOUT_LINK = (By.ID, "logout_sidebar_link")

    def is_loaded(self):
        return self.text_of(self.TITLE) == "Products"

    def add_item_to_cart(self, item_id):
        self.click((By.ID, f"add-to-cart-{item_id}"))

    def remove_item_from_cart(self, item_id):
        self.click((By.ID, f"remove-{item_id}"))
        # The badge disappears once the cart is empty again.
        self.wait_until_gone(self.CART_BADGE)

    def cart_count(self):
        badges = self.find_all(self.CART_BADGE)
        return int(badges[0].text) if badges else 0

    def open_cart(self):
        self.js_click(self.CART_LINK)
        self.wait_for_url_to_contain("cart")

    def sort_by(self, option_value):
        Select(self.find(self.SORT_DROPDOWN)).select_by_value(option_value)

    def listed_prices(self):
        return [float(el.text.replace("$", "")) for el in self.find_all(self.ITEM_PRICES)]

    def logout(self):
        self.click(self.MENU_BUTTON)
        self.js_click(self.LOGOUT_LINK)
        self.wait_for_url_to_be(self.BASE_URL + "/")
