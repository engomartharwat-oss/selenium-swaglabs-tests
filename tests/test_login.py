import pytest
from pages.login_page import LoginPage
from pages.inventory_page import InventoryPage


def test_valid_user_can_log_in(driver):
    """A user with correct credentials reaches the products page."""
    login = LoginPage(driver).open()
    login.login("standard_user", "secret_sauce")
    assert InventoryPage(driver).is_loaded()
    assert "inventory" in driver.current_url


def test_locked_out_user_sees_an_error(driver):
    """A locked-out user stays on the login page with a clear error."""
    login = LoginPage(driver).open()
    login.login("locked_out_user", "secret_sauce")
    assert "locked out" in login.error_text()


@pytest.mark.parametrize("username,password", [
    ("standard_user", "wrong_password"),
    ("not_a_real_user", "secret_sauce"),
])
def test_invalid_credentials_show_an_error(driver, username, password):
    """Wrong username or password is rejected with an error message."""
    login = LoginPage(driver).open()
    login.login(username, password)
    assert "do not match" in login.error_text()
