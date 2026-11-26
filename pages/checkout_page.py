from selenium.webdriver.common.by import By
from .base_page import BasePage


class CheckoutPage(BasePage):
    """The checkout form, overview and confirmation pages."""

    FIRST_NAME = (By.ID, "first-name")
    LAST_NAME = (By.ID, "last-name")
    POSTAL_CODE = (By.ID, "postal-code")
    CONTINUE_BUTTON = (By.ID, "continue")
    FINISH_BUTTON = (By.ID, "finish")
    COMPLETE_HEADER = (By.CSS_SELECTOR, ".complete-header")

    def fill_customer_info(self, first, last, postal_code):
        self.type_text(self.FIRST_NAME, first)
        self.type_text(self.LAST_NAME, last)
        self.type_text(self.POSTAL_CODE, postal_code)
        self.click(self.CONTINUE_BUTTON)

    def finish(self):
        self.js_click(self.FINISH_BUTTON)
        self.wait_for_url_to_contain("checkout-complete")

    def confirmation_text(self):
        return self.text_of(self.COMPLETE_HEADER)
