import os
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options


@pytest.fixture
def driver():
    """Start a headless Chrome browser and close it after each test."""
    options = Options()
    options.add_argument("--headless=new")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--window-size=1280,800")
    # Selenium Manager downloads the right ChromeDriver automatically.
    browser = webdriver.Chrome(options=options)
    browser.implicitly_wait(5)
    yield browser
    browser.quit()


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """Save a screenshot when a test fails, so debugging is easier."""
    outcome = yield
    report = outcome.get_result()
    if report.when == "call" and report.failed:
        driver = item.funcargs.get("driver")
        if driver:
            os.makedirs("reports/screenshots", exist_ok=True)
            path = f"reports/screenshots/{item.name}.png"
            driver.save_screenshot(path)
