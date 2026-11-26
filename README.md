# Swag Labs UI Test Automation (Java, Selenium, TestNG, Maven)

A UI test automation framework for the public demo shop [saucedemo.com](https://www.saucedemo.com).
It has **33 test methods that run as 42 test cases** (some methods run several times with different data).
All 42 pass against the live site.

## Tech stack

| Tool | Use |
|------|-----|
| Java 11 | Language |
| Selenium WebDriver 4 | Browser automation (Selenium Manager downloads the driver automatically) |
| TestNG | Test runner, data providers, parallel run |
| Maven (multi-module) | Build and dependencies |
| ExtentReports | HTML report with a screenshot link for every failure |

## Project structure

```
selenium-swaglabs-tests
├── pom.xml                      parent POM (versions, modules)
├── framework-core               reusable framework, knows nothing about Swag Labs
│   └── src/main/java/com/omartharwat/framework
│       ├── config/ConfigReader        reads config.properties, can be overridden with -D flags
│       ├── driver/DriverFactory       creates Chrome / Firefox / Edge (headless option)
│       ├── driver/DriverManager       one WebDriver per thread (safe for parallel runs)
│       ├── pages/BasePage             click, type, read text, explicit waits
│       ├── utils/WaitUtils            explicit waits (no Thread.sleep anywhere)
│       ├── utils/ScreenshotUtils      saves PNG screenshots
│       ├── utils/CsvReader            loads CSV files for data-driven tests
│       ├── listeners/TestListener     TestNG listener: report + screenshot on failure
│       └── reporting/ExtentManager    builds the HTML report
└── ui-tests                     the Swag Labs tests
    ├── src/main/java/.../pages        Page Object Model: Login, Inventory, ProductDetails, Cart, Checkout x3, Menu
    ├── src/main/resources/config.properties   URL, browser, timeouts, demo users
    ├── src/test/java/.../tests        test classes
    └── src/test/resources
        ├── testng.xml                 suite file (5 classes in parallel)
        └── testdata/*.csv             data for data-driven tests
```

Design ideas: page objects hold locators and actions, tests hold only the checks (assertions); the
framework module is separate so it can be reused for another website.

## How to run

You need Java 11+, Maven 3.9+ and Google Chrome.

```bash
mvn clean test                       # headless Chrome (default)
mvn clean test -Dheadless=false      # watch the browser
mvn clean test -Dbrowser=firefox     # Firefox
```

After a run:
- HTML report: `ui-tests/target/extent-report.html`
- Failure screenshots: `ui-tests/target/screenshots/`
- TestNG/Surefire results: `ui-tests/target/surefire-reports/`

## The tests in plain words

### LoginTests (6 methods, 11 cases)
| Test | What it checks |
|------|----------------|
| loginFormIsDisplayed | Username, password and Login button are visible |
| standardUserCanLogIn | A valid user reaches the Products page |
| allowedUsersCanLogIn (x3) | standard_user, problem_user and performance_glitch_user can all log in |
| lockedOutUserSeesError | The locked out user sees "this user has been locked out" |
| invalidLoginShowsError (x4, from CSV) | Wrong password, unknown user, empty username, empty password each show the correct error |
| errorMessageCanBeDismissed | The red error banner closes with the X button |

### InventoryTests (7 methods, 9 cases)
| Test | What it checks |
|------|----------------|
| inventoryShowsSixProducts | The shop shows 6 products |
| defaultSortIsNameAscending | Default order is A to Z |
| sortByNameDescending | Sorting Z to A works |
| sortByPriceAscending | Low to high works, cheapest is $7.99 |
| sortByPriceDescending | High to low works, most expensive is $49.99 |
| productDetailsPageShowsCorrectData (x3) | Name, price and description on the product page match |
| backToProductsFromDetails | "Back to products" returns to the list |

### CartTests (9 methods)
| Test | What it checks |
|------|----------------|
| addSingleItemShowsBadge | Cart badge shows 1 and the button turns into Remove |
| addMultipleItemsUpdatesBadge | Three items give badge 3 |
| removeItemFromInventoryClearsBadge | Remove on the Products page clears the badge |
| cartPageListsAddedItems | The cart lists exactly the items that were added |
| emptyCartHasNoItems | A new cart is empty |
| removeItemFromCartPage | Remove on the cart page works |
| continueShoppingReturnsToInventory | Continue Shopping goes back to Products |
| cartSurvivesRefresh | The cart keeps its items after a page refresh |
| addFromProductDetailsPage | Add to cart from the product page updates the badge |

### CheckoutTests (6 methods, 8 cases)
| Test | What it checks |
|------|----------------|
| fullPurchaseEndToEnd | Login, add item, checkout, finish, "Thank you for your order!" |
| cartIsEmptyAfterPurchase | The cart is empty after buying |
| checkoutFormValidation (x3, from CSV) | Missing first name, last name or postal code shows the right error |
| overviewListsCartItems | The overview shows the same items as the cart |
| overviewTotalsAreCorrect | Item total equals the sum of prices, total equals item total plus tax |
| cancelCheckoutReturnsToCart | Cancel returns to the cart with the item still there |

### SessionTests (5 methods)
| Test | What it checks |
|------|----------------|
| logoutReturnsToLoginPage | Logout from the menu shows the login page |
| cannotOpenInventoryWithoutLogin | Opening the Products URL without logging in is blocked with an error |
| backButtonAfterLogoutDoesNotRestoreSession | Browser Back after logout does not show the shop |
| resetAppStateEmptiesCart | "Reset App State" in the menu empties the cart |
| menuAllItemsOpensProducts | "All Items" in the menu opens Products |

## Notes
- The site `saucedemo.com` is a public practice site and its login details are published on its login page.
- Every test starts a fresh browser, so tests do not depend on each other.
