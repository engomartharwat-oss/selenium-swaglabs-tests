from selenium.webdriver.common.by import By
from .base_page import BasePage


class LoginPage(BasePage):
    """The Swag Labs login page."""

    USERNAME = (By.ID, "user-name")
    PASSWORD = (By.ID, "password")
    LOGIN_BUTTON = (By.ID, "login-button")
    ERROR_MESSAGE = (By.CSS_SELECTOR, "h3[data-test='error']")

    def open(self):
        self.driver.get(self.BASE_URL)
        return self

    def login(self, username, password):
        self.type_text(self.USERNAME, username)
        self.type_text(self.PASSWORD, password)
        self.click(self.LOGIN_BUTTON)

    def error_text(self):
        return self.text_of(self.ERROR_MESSAGE)
