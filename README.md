# Page Object Design Pattern with Selenium PageFactory
This section is about Page Object Model Framework which is also known as Page Object Design Pattern or Page Object.
The main advantage of Page Object Model is that if the UI or any HTML object changes for any page,
the test does not need any fix. Only the code within the page objects will be affected but it does not have any impact
on the test code.

Till now we have just one test and one step definition file. In real live projects we will be dealing with hundreds of tests
which all have multiple Step Definition files. The whole project code will become unmanageable and unmaintainable.
To better manage the code and to improve the re-usability, Page Object Design pattern suggests us to divide an application
to sub pages / sections. So far we were writing a code with no actual structure, focusing only on elements and sending commands
to Selenium driver.

The Page Object Pattern technique provides a solution for working with multiple web pages. It will help prevent unwanted
code duplication and enable an effective solution for a code maintenance. In general, every page of the application involved
in our end-to-end testing will be represented by a unique class of its own. Such class will include both page element inspection
and associated actions performed by Selenium on the corresponding page.

In order to implement the Page Object Model we will be using **Selenium PageFactory**

Selenium PageFactory is an inbuilt Page Object Model concept for Selenium WebDriver and it is very optimized.
PageFactory is used to Initialise Elements of a Page class without having to use ‘FindElement()‘ or ‘FindElements()‘ methods.
Annotations can be used to supply descriptive names of target objects to improve code readability.

**@FindBy Annotation:**

As the name suggest, it helps to find the elements in the page using By strategy.
@FindBy can accept TagName, PartialLinkText, Name, LinkText, Id, Css, ClassName and XPath as an attribute.
```
@FindBy(id = “idname“)]
public WebElement element;
```
The above code will create a PageObject and name it 'element' by finding it using its 'id' locator.

**InitElements:**

This Instantiate an Instance of the given class. This method will attempt to instantiate the class given to it,
preferably using a constructor which takes a WebDriver instance as its only argument
An exception will be thrown if the class cannot be instantiated.
```
PageFactory.initElements(WebDriver, PageObject.Class);
```
**Parameters:**

- WebDriver – The driver that will be used to look up the elements

- PageObject  – A class which will be initialised

**Returns:**
An instantiated instance of the class with WebElement and List<WebElement> fields proxies

**PageFactory NameSpace:**

In order to use PageFactory, *org.openqa.selenium.support.PageFactory* needs to be imported to the associated Class.

## Step 1: Create Page Object Classes
The flow of our test spreads across the following pages:
- Home Page
- Product Listing Page
- Cart Page
- Checkout Page
- Confirmation Page

Therefore, we will be creating corresponding Java Classes in our Project like so:
- HomePage.java
- ProductListingPage.java
- CartPage.java
- CheckoutPage.java
-ConfirmationPage.java

1) Create a New Package file and name it 'pageObjects', by right click on the src/main/java and select New >> Package.

2) Create five New Class files and name them as was mentioned above
3) Initiate the Page Object for each Class using Constructor. Example of CheckoutPage initiation:
```
public CheckoutPage(WebDriver driver) {
     PageFactory.initElements(driver, this);
}
```
4) Move  WebElement locators associated with a particular page to a corresponding Page Object Class
and replace 'findElement(s) (By by)' method with @FindBy annotation.
5) Wrap Selenium actions performed on each page into re-usable methods and again, place them into corresponding Page Object Class.

Note: ConfirmationPage.java is the only Class which we leave blank as we have no implementation of @Then Step yet

Newly created **Page Object** Classes should looks like this:

### HomePage.java
```
package pageObjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
        driver.get("http://www.shop.demoqa.com");
    }

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}

```
### ProductListingPage.java
```
package pageObjects;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class ProductListingPage {

    public ProductListingPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "button.single_add_to_cart_button")
    public WebElement btn_AddToCart;

    @FindAll(@FindBy(css = ".noo-product-inner"))
    public List<WebElement> prd_List;

    @FindBy(id="pa_color")
    public WebElement selectColour;

    @FindBy(id="pa_size")
    public WebElement selectSize;

    public void select_Product(int productNumber) {
        prd_List.get(productNumber).click();
    }

    public void makeSelection(int index) {
        Select colour = new Select(selectColour);
        colour.selectByIndex(index);
        Select size  = new Select(selectSize);
        size.selectByIndex(index);
    }

    public void clickOn_AddToCart() {
        btn_AddToCart.click();
    }
}

```
### CartPage.java
```
package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {

    public CartPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".cart-button")
    public WebElement btn_Cart;

    @FindBy(css = ".checkout-button.alt")
    public WebElement btn_ContinueToCheckout;

    public void clickOn_Cart() {
        btn_Cart.click();
    }

    public void clickOn_ContinueToCheckout(){
        btn_ContinueToCheckout.click();
    }
}
```
### CheckoutPage.java
```
package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage {

    public CheckoutPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "billing_first_name")
    public WebElement txtbx_FirstName;

    @FindBy(id = "billing_last_name")
    public WebElement txtbx_LastName;

    @FindBy(id = "billing_email")
    public WebElement txtbx_Email;

    @FindBy(id = "billing_phone")
    public WebElement txtbx_Phone;

    @FindBy(id = "billing_country")
    public WebElement select_Country;

    @FindBy(id = "billing_city")
    public WebElement txtbx_City;

    @FindBy(id = "billing_address_1")
    public WebElement txtbx_Address;

    @FindBy(css = "#billing_postcode")
    public WebElement txtbx_PostCode;

    @FindBy(css = ".woocommerce-form__input-checkbox")
    public WebElement chkbx_AcceptTermsAndCondition;

    @FindBy(id = "place_order")
    public WebElement btn_PlaceOrder;

    public void enter_Name(String name) {
        txtbx_FirstName.sendKeys(name);
    }

    public void enter_LastName(String lastName) {
        txtbx_LastName.sendKeys(lastName);
    }

    public void enter_Email(String email) {
        txtbx_Email.sendKeys(email);
    }

    public void enter_Phone(String phone) {
        txtbx_Phone.sendKeys(phone);
    }

    public void enter_City(String city) {
        txtbx_City.sendKeys(city);
    }

    public void enter_Address(String address) {
        txtbx_Address.sendKeys(address);
    }

    public void enter_PostCode(String postCode) {
        txtbx_PostCode.sendKeys(postCode);
    }

    public void select_Country(String countryName) {
        Select country = new Select(select_Country);
        country.selectByVisibleText(countryName);
    }

    public void check_TermsAndCondition() {
        chkbx_AcceptTermsAndCondition.click();
    }

    public void clickOn_PlaceOrder() {
        btn_PlaceOrder.submit();
    }

    public void fill_PersonalDetails() throws InterruptedException {
        enter_Name("TestAutomation");
        enter_LastName("Opencast");
        select_Country("United Kingdom (UK)");
        enter_Address("Hoults Yard, Walker Road");
        enter_City("Newcastle upon Tyne");
        enter_PostCode("NE6 3PE");
        Thread.sleep(2000);
        enter_Phone("07438862327");
        enter_Email("test@test.com");
    }
}
```
### ConfirmationPage.java
```
package pageObjects;

public class ConfirmationPage {
}
```
## Step 2: Implement Page Objects in Step Definition file
It should look like this:

### Steps.java
```
package stepDefinitions;

import java.util.concurrent.TimeUnit;
import cucumber.api.java.en.Then;
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

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver","src/drivers/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        homePage = new HomePage(driver);
        homePage.navigateTo_HomePage();
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        Thread.sleep(1000);
        homePage.perform_Search("dress");
        Thread.sleep(1000);
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() throws InterruptedException {
        productListingPage = new ProductListingPage(driver);
        Thread.sleep(1000);
        productListingPage.select_Product(0);
        productListingPage.makeSelection(1);
        productListingPage.clickOn_AddToCart();
    }

    @When("I move to checkout from mini cart")
    public void i_move_to_checkout_from_mini_cart() throws InterruptedException{
        cartPage = new CartPage(driver);
        Thread.sleep(1000);
        cartPage.clickOn_Cart();
        cartPage.clickOn_ContinueToCheckout();
    }

    @When("I enter my personal details")
    public void i_enter_my_personal_details() throws InterruptedException {
        checkoutPage = new CheckoutPage(driver);
        Thread.sleep(1000);
        checkoutPage.fill_PersonalDetails();
    }

    @When("I place the order")
    public void i_place_the_order() throws InterruptedException {
        checkoutPage = new CheckoutPage(driver);
        Thread.sleep(1000);
        checkoutPage.check_TermsAndCondition();
        checkoutPage.clickOn_PlaceOrder();
    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {
        System.out.println("Not implemented");
        driver.manage().deleteAllCookies();
        driver.close();
        driver.quit();
    }
}

```
Run TestRunner and the test should be executed successfully