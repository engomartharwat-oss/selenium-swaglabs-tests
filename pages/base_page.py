from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


class BasePage:
    """Shared helpers every page object uses."""

    BASE_URL = "https://www.saucedemo.com"

    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, 10)

    def find(self, locator):
        return self.wait.until(EC.visibility_of_element_located(locator))

    def find_all(self, locator):
        return self.driver.find_elements(*locator)

    def click(self, locator):
        # Click through JavaScript: the demo site's React event handlers
        # occasionally ignore a plain mouse click in headless Chrome.
        element = self.wait.until(EC.element_to_be_clickable(locator))
        self.driver.execute_script("arguments[0].click();", element)

    def js_click(self, locator):
        self.click(locator)

    def type_text(self, locator, text):
        field = self.find(locator)
        field.clear()
        field.send_keys(text)

    def text_of(self, locator):
        return self.find(locator).text

    def wait_for_url_to_contain(self, fragment):
        self.wait.until(EC.url_contains(fragment))

    def wait_for_url_to_be(self, url):
        self.wait.until(EC.url_to_be(url))

    def wait_until_gone(self, locator):
        self.wait.until(EC.invisibility_of_element_located(locator))
