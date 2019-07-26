# File Reader Manager as a Singleton Design Pattern
In the previous section, we run into a problem of having multiple instances of ConfigFileReader Class in our project.
In this section we will use File Reader Manager as Singleton Design Pattern to eliminate the issue.
Singleton Design Pattern helps in achieving that we only have one instance of a class which can be accessed globally.

### What is a Singleton Design Pattern?
The Singleton’s purpose is to control object creation, limiting the number of objects to only one.
Since there is only one Singleton instance, any instance fields of a Singleton will occur only once per class,
just like static fields.

### How to implement Singleton Pattern?
To implement Singleton pattern, we have to implement the following concept:

- **Private constructor** to restrict instantiation of the class from other classes.
- **Private static variable** of the same class that is the only instance of the class.
- **Public static method** that returns the instance of the class, this is the global access point
to get the instance of the singleton class.

## Step 1: Create File Reader Manager as Singleton Design Pattern
1) Create a New Class in src/main/java inside 'managers' package and name it 'FileReaderManager'.
2) Add the following code so **File Reader Manager** looks like this:
### FileReaderManager.java
```
package managers;

import dataProviders.ConfigFileReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;

    private FileReaderManager() {
    }

    public static FileReaderManager getInstance( ) {
        return fileReaderManager;
    }

    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }
}
```

## Explanation
**FileReaderManager class** maintains a static reference to its own instance and returns that reference
from the static getInstance() method.

**FileReaderManager** implements a private constructor so clients cannot instantiate FileReaderManager instances.

**getConfigReader()** method returns the instance of the **ConfigFileReader** but this method is **not static**.
So that client has to use the **getInstance()** method to access other public methods of the FileReaderManager like
```
FileReaderManager.getInstance().getConfigReader()
```
## Step 2: Implement File Reader Manager in test code (Steps.java and HomePage.java)
### Steps.java
```
package stepDefinitions;

import cucumber.api.java.en.Then;
import managers.PageObjectManager;
import managers.FileReaderManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import pageObjects.CartPage;
import pageObjects.CheckoutPage;
import pageObjects.HomePage;
import pageObjects.ProductListingPage;
import java.util.concurrent.TimeUnit;


public class Steps {
    WebDriver driver;
    HomePage homePage;
    ProductListingPage productListingPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    PageObjectManager pageObjectManager;

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver", FileReaderManager.getInstance().getConfigReader().getDriverPath());
        driver = new ChromeDriver();
        pageObjectManager = new PageObjectManager(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().getConfigReader().getImplicitWait(), TimeUnit.SECONDS);
        homePage = pageObjectManager.getHomePage();
        homePage.navigateTo_HomePage();
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.perform_Search("dress");
        Thread.sleep(1000);
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() throws InterruptedException {
        Thread.sleep(1000);
        productListingPage = pageObjectManager.getProductListingPage();
        productListingPage.select_Product(0);
        productListingPage.makeSelection(1);
        productListingPage.clickOn_AddToCart();
    }

    @When("I move to checkout from mini cart")
    public void i_move_to_checkout_from_mini_cart() throws InterruptedException{
        Thread.sleep(1000);
        cartPage = pageObjectManager.getCartPage();
        cartPage.clickOn_Cart();
        cartPage.clickOn_ContinueToCheckout();
    }

    @When("I enter my personal details")
    public void i_enter_my_personal_details() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage = pageObjectManager.getCheckoutPage();
        checkoutPage.fill_PersonalDetails();
    }

    @When("I place the order")
    public void i_place_the_order() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.check_TermsAndCondition();
        checkoutPage.clickOn_PlaceOrder();
        driver.manage().deleteAllCookies();
        driver.close();
        driver.quit();
    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {
        System.out.println("Not implemented");
    }
}
```
### HomePage.java
```
package pageObjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import managers.FileReaderManager;

public class HomePage {

    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void navigateTo_HomePage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
    }

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}

```
Run TestRunner and the test should be executed successfully





