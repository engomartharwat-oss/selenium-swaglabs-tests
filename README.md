# Selenium Test Automation - Swag Labs

A small but complete test automation framework written in Python, testing the public demo shop [Swag Labs](https://www.saucedemo.com).

It shows the way I structure real automation work: the **Page Object Model** (every page of the site is a Python class, so tests stay short and readable), **pytest** fixtures that open and close the browser for each test, **explicit waits** instead of fixed sleeps, an **HTML report** after every run, and an automatic **screenshot whenever a test fails**.

## Tech stack

- Python 3
- Selenium WebDriver 4 (ChromeDriver is downloaded automatically by Selenium Manager)
- pytest
- pytest-html (generates `reports/report.html`)

## Project layout

```
pages/       Page Object classes: one class per page of the site
tests/       The test cases, grouped by feature
conftest.py  Browser fixture + screenshot-on-failure hook
pytest.ini   Test discovery and HTML report settings
```

## How to run

```bash
pip install -r requirements.txt
pytest
```

The report opens at `reports/report.html`. A sample report from a real run is included in the repo.

## The test cases, in plain words

### Login (`tests/test_login.py`)
- **Valid login** - enter the right username and password, and check the user lands on the products page. This is the most important path in any shop.
- **Locked-out user** - the site has a user that is blocked. The test confirms the site refuses the login and shows the "locked out" message, not a crash or a silent failure.
- **Wrong credentials** - same check for a wrong password and for a username that does not exist. I run both cases through one test using pytest parametrization, so adding more cases is one line.

### Products and cart (`tests/test_inventory.py`)
- **Add to cart** - add a product and check the cart badge shows 1.
- **Remove from cart** - remove it again and check the badge disappears.
- **Sort by price** - choose "Price (low to high)" and verify the prices on screen are really in ascending order, by comparing the list against its sorted copy.
- **Cart contents** - add a specific product and check the cart page lists exactly that product by name.
- **Logout** - log out through the side menu and check the user is back on the login page.

### Checkout (`tests/test_checkout.py`)
- **Full purchase, end to end** - log in, add a product, open the cart, fill in the checkout form, finish, and verify the "Thank you for your order" confirmation appears. This one test walks the whole business flow the way a real user would.

## What I would add next

- Run the same suite in CI (GitHub Actions) on every push
- Run tests in parallel with pytest-xdist
- Data-driven tests reading test data from an external file
