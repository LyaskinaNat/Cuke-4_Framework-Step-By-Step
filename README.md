# Page Object Manager
In this section we will design a Page Object Manager.
In previous section we have created objects for our pages like so:
```
HomePage homePage = new HomePage(driver);
ProductListingPage productListingPage = new ProductListingPage(driver);
cartPage = new CartPage(driver);
checkoutPage = new CheckoutPage(driver);
```

But there is a problem here. So far we have just one single Cucumber Step Definition file.
But in the case of multiple step definition files, we will be creating object of Pages again and again.
This is against the coding principle.

To avoid this situation, we can create a **Page Object Manager**.

The purpose of the Page Object Manger is to create a page object and also to make sure that the object
is only created once and it can be used across all step definition files.

## Step 1: Design Page Object Manager Class
1) Create a New Package in src/main/java and name it 'managers'.

2) Create a New Class inside 'managers' package and name it 'PageObjectManager'.
3) Add the following code to the class
### PageObjectManager.java
```
package managers;

import org.openqa.selenium.WebDriver;
import pageObjects.CartPage;
import pageObjects.CheckoutPage;
import pageObjects.ConfirmationPage;
import pageObjects.HomePage;
import pageObjects.ProductListingPage;

public class PageObjectManager {

    private WebDriver driver;
    private ProductListingPage productListingPage;
    private CartPage cartPage;
    private HomePage homePage;
    private CheckoutPage checkoutPage;
    private ConfirmationPage confirmationPage;

    // Constructor
    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    //Creating an Object of Page Class only if the object is null,
    //Supplying the already created object if it is not null:

    public HomePage getHomePage(){
        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
    }

    public ProductListingPage getProductListingPage() {
        return (productListingPage == null) ? productListingPage = new ProductListingPage(driver) : productListingPage;
    }

    public CartPage getCartPage() {
       return (cartPage == null) ? cartPage = new CartPage(driver) : cartPage;
    }

    public CheckoutPage getCheckoutPage() {
        return (checkoutPage == null) ? checkoutPage = new CheckoutPage(driver) : checkoutPage;
    }

}
```
## Explanation
 **Constructor:**
```
public PageObjectManager(WebDriver driver) {
     this.driver = driver;
}
```
This constructor is asking for parameter of type WebDriver: to create an object of the Pages, this class requires a driver.

Therefore to create an object of PageObjectManager class, driver also needs to be provided:
```
PageObjectManager pageObjectManager = new PageObjectManager(driver);
```
**Page Object Creation Method:**
```
public HomePage getHomePage() {
     return (homePage == null) ? new HomePage(driver) : homePage;
}
```
This method has two responsibilities:

- To create an Object of Page Class only if the object is null.
- To supply the already created object if it is not null

## Step 2: Modify Step Definition File
Implementation of PageObjectManager requires change in our step definition file as well.
Now the duty of the creation of all the pages assigned to only one class which is Page Object Manager.
### Steps.java
```
package stepDefinitions;

import java.util.concurrent.TimeUnit;
import cucumber.api.java.en.Then;
import managers.PageObjectManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import pageObjects.CartPage;
import pageObjects.CheckoutPage;
import pageObjects.HomePage;
import pageObjects.ProductListingPage;


public class Steps {
    WebDriver driver;
    HomePage homePage;
    ProductListingPage productListingPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    PageObjectManager pageObjectManager;


    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver","src/drivers/chromedriver");
        driver = new ChromeDriver();
        pageObjectManager = new PageObjectManager(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
Run TestRunner and the test should be executed successfully
