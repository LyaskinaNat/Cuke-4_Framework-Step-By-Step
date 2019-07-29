# INTRODUCTION

This tutorial is for Cucumber Automation Framework using Maven and Java.

This is an advanced topic and is required basic knowledge and experience
in Java, Maven, Cucumber and Gherkin syntax.

This is a step-by-step work through which starts with the basic End-to-to-end test implementation
and builds it up into sophisticated and effectively maintainable test automation framework.

Each section is a logical continuation of a previous one. It results in a fully working test automation at the end of each section.

The tutorial must be followed in a consequent order.
The following topics will be covered:
 - Preparation for framework building: Implementing Cucumber end-to-end test automation
 - Introduction to Page Object Design Pattern with Selenium PageFactory
 - Implementation of Page Object Manager
 - Learning how to read Project Configurations from Property File
 - Making File Reader Manager as a Singleton Design Pattern
 - Introduction of a WebDriver Manager
 - Learning how to share Test Context between steps and Step Definition files
 - Implementation of Hooks in Cucumber Framework
 - Learning how to use Data tables in Cucumber versions 3 and above
 - Learning about Implicit and Explicit Wait in Selenium WebDriver
 - Learning how to share data between steps in Cucumber using Scenario Context
 - Introduction to Data Driven Testing and how to read test data from Json file
 - Introduction to Cucumber Reports
 - Learning about Third parties reporting tools and features by implementing Cucumber Extent Report Adapter


_________________________________________________________________________________________________________________________

# SECTION 1: Cucumber end-to-end test
## Step 1: Create a resources folder
1) Delete App.java and AppTest.java files as they are just sample project files created by default by Maven.
To delete the files, just right click on the package and select Delete.
2) Create a new resources folder under src/test/. As a standard we keep Cucumber feature files in resources folder.
Right click on the src/test/java and create a New Package and specify the name as resources.
 -  For Eclipse IDE - right click on the root project and select Maven >> Update Project.

 (Note: In Eclipse, if any changes are made to Maven POM or any folder structure,  always update project Maven >> Update Project
 to reflect the latest changes)
 -   For IntelliJ IDE  -  right click on the resource folder and choose Mark directory as >> Test resources root.
 (Note: In IntelliJ, enable autoupdate  - prompt should pop up on the bottom-right corner)
## Step 2: Add Selenium and JUnit to project
1) Selenium: selenium-java; version 3.141.59
2) JUnit: junit; version 4.12
## Step 3: Add Cucumber Dependencies to the Project
1) cucumber-java, version 4.2.0
2) cucumber-junit, version 4.2.0
(Note: Cucumber-java and cucumber-junit dependencies required to be of the same version.
Properties tag can be used in pom.xml to create a variable for cucumber related dependencies for easier upgrading)
## Step 4: Set up Maven Compiler Plugin
The Compiler Plugin is used to compile the sources of your project.
Also note that at present the default source setting is 1.5 and the default target setting is 1.5, independently of the JDK
you run Maven with. If you want to change these defaults, you should set source and target as described
in Setting the –source and –target of the Java Compiler.
maven-compiler-plugin; version 3.7.0

Pom.xml should look like this:
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>Cuke-4-Framework</groupId>
    <artifactId>Cuke-4-Framework</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <cucumber.version>4.2.0</cucumber.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.141.59</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <source>1.8</source>
                    <target>1.8</target>
                    <compilerArgument>-Werror</compilerArgument>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

## Step 5: Add Chrome driver to the project
1) Download Chrome driver from http://chromedriver.chromium.org/downloads
(Note: make sure the version of Chrome driver matches the Chrome browser version on you PC)
2) Create a New Package and name it 'drivers' by right click on the src and select New >> Package
3) Place Chrome driver from your Download folder to project's src/drivers folder
## Step 6: Write End 2 End test in a Feature file
For the purpose of this tutorial, we use the test longer than the usual. This is to demonstrate right examples of framework
components.  Cucumber Framework requires to have complex page objects, various configurations and challenges.
This end-to-end scenario could be viewed as a business scenario to automate and it will help us
demonstrate Cucumber framework implementation
1) Test to Automate
User visits Demo Website and searches for a Dress. User selects the first product and goes to product page.
User successfully adds it to the bag. User continues to Cart Page from mini cart icon at the top right corner.
Then user moves forward to Checkout page and order details. User fills in required information, accepts Terms and conditions
and proceeds with the order. User is presented with order confirmation including details on the purchased dress.
2) Create Feature File:
2.1) Create a New Package and name it 'features', by right click on the src/test/resources and select New >> Package.
(Note: It is always recommended to put all the feature files in the resources folder).
2.2) Create a Feature file and name it as End2End_Test.feature by right click on the above created package
and select New >> File.
(Note: all feature files must have .feature extension)
3) Add the test steps to the feature file as follows:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Scenario: Customer place an order by purchasing an item from search
    Given user is on Home Page
    When he search for "dress"
    And choose to buy the first item
    And moves to checkout from mini cart
    And enter customer personal details
    And place the order
    Then verify the order details
```

##  Step 7: Create a JUnit Test Runner
 1) Create a New Package and name it as runners by right click on the src/test/java and select New >> Package.
 2) Create a New Java Class file and name it as TestRunner by right click on the above created package and select New >> Class.

(Note: It is important to have a key word 'Test' as a part of a runner class name, so test(s) can be run from the command line using Maven)
## Step 8: Write test code to Step file
To get the steps automatically generated, we need to execute TestRunner class.
Right click on the TestRunner file and select

```
Eclipse: Run As >> JUnit Test
IntelliJ: Run TestRunner
```

You would get the below result in the IDE Console:

```
Undefined scenarios:
src/test/resources/features/End2End_Test.feature:4 # Customer place an order by purchasing an item from search

1 Scenarios (1 undefined)
7 Steps (7 undefined)
0m0.399s

You can implement missing steps with the snippets below:

@Given("I am on Home Page")
public void i_am_on_Home_Page() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@When("I search for product in dress category")
public void i_search_for_product_in_dress_category() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@When("I choose to buy the first item")
public void i_choose_to_buy_the_first_item() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@When("I move to checkout from mini cart")
public void i_move_to_checkout_from_mini_cart() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@When("I enter my personal details")
public void i_enter_my_personal_details() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@When("I place the order")
public void i_place_the_order() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

@Then("Order details are successfully verified")
public void order_details_are_successfully_verified() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

Process finished with exit code 0

```
2) Create a New Package and name it 'stepDefinitions' by right click on the src/test/java and select New >> Package.
3) Create a New Java Class and name it is 'Steps' by right click on the above created package and select New >> Class.
4) Now copy all the steps created by IDE to this Steps file and start filling up these steps with Selenium Code.
Steps test file will look like this:

```
package stepDefinitions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openqa.selenium.support.ui.Select;

public class Steps {
    WebDriver driver;

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        //Get Chrome driver
        System.setProperty("webdriver.chrome.driver","src/drivers/chromedriver");
        driver = new ChromeDriver();
        //Open Chrome driver
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //Navigate to Home page
        driver.get("http://www.shop.demoqa.com");
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        //Explicit wait is added to wait for elements to load on a page
        Thread.sleep(2000);

        //On Home page, search for "dress" product category
        WebElement btn_search = driver.findElement(By.cssSelector(".noo-search"));
        btn_search.click();
        Thread.sleep(2000);
        WebElement input_search = driver.findElement(By.cssSelector("input.form-control"));
        input_search.sendKeys("dress");
        input_search.sendKeys(Keys.RETURN);
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() throws InterruptedException {
        Thread.sleep(2000);
        //On Product page, get all items displayed on the page from the search result
        List<WebElement> items = driver.findElements(By.cssSelector(".noo-product-inner"));
        //Click on the first item
        items.get(0).click();
        //Select colour and size
        WebElement select_colour = driver.findElement(By.id("pa_color"));
        Select colour = new Select(select_colour);
        colour.selectByIndex(1);
        WebElement select_size = driver.findElement(By.id("pa_size"));
        Select size  = new Select(select_size);
        size.selectByIndex(1);
        //Add item to cart
        WebElement addToCart = driver.findElement(By.cssSelector("button.single_add_to_cart_button"));
        addToCart.click();
    }

    @When("I move to checkout from mini cart")
    public void i_move_to_checkout_from_mini_cart() throws InterruptedException{
        Thread.sleep(2000);
        //On Cart page, click on Cart element
        WebElement cart = driver.findElement(By.cssSelector(".cart-button"));
        cart.click();
        Thread.sleep(2000);
        //And click on checkout button
        WebElement continueToCheckout = driver.findElement(By.cssSelector(".checkout-button.alt"));
        continueToCheckout.click();
    }

    @When("I enter my personal details")
        public void i_enter_my_personal_details() throws InterruptedException {
            Thread.sleep(2000);
            //On Checkout page, fill in customer details
            WebElement firstName = driver.findElement(By.id("billing_first_name"));
            firstName.sendKeys("TestAutomation");

            WebElement lastName = driver.findElement(By.id("billing_last_name"));
            lastName.sendKeys("Opencast");
            WebElement select_Country = driver.findElement(By.id("billing_country"));
            Select country = new Select(select_Country);
            country.selectByVisibleText("United Kingdom (UK)");

            WebElement address = driver.findElement(By.id("billing_address_1"));
            address.sendKeys("Hoults Yard, Walker Road");

            WebElement city = driver.findElement(By.id("billing_city"));
            city.sendKeys("Newcastle upon Tyne");
            WebElement postcode = driver.findElement(By.id("billing_postcode"));
            postcode.sendKeys("NE6 3PE");
            //Page gets refreshed after the postcode is entered, so we introduce an extra wait
            Thread.sleep(2000);

            WebElement phone = driver.findElement(By.id("billing_phone"));
            phone.sendKeys("07438862327");
            WebElement emailAddress = driver.findElement(By.id("billing_email"));
            emailAddress.sendKeys("test@test.com");

        }

        @When("I place the order")
        public void i_place_the_order() throws InterruptedException {
            Thread.sleep(2000);
            //On Checkout page, click on T&Cs and submit the order
            WebElement chkbx_AcceptTermsAndCondition = driver.findElement(By.cssSelector(".woocommerce-form__input-checkbox"));
            chkbx_AcceptTermsAndCondition.click();
            WebElement btn_PlaceOrder = driver.findElement(By.id("place_order"));
            btn_PlaceOrder.submit();
            Thread.sleep(2000);
        }

        @Then("Order details are successfully verified")
        public void order_details_are_successfully_verified() {
           //User is automatically re-directed to the Order confirmation page. Validation step will be implemented later on this course
            System.out.println("Not implemented");
            //Closing the browser
            driver.manage().deleteAllCookies();
            driver.close();
            driver.quit();
        }

}

```

5) Update TestRunner class

We also need to make sure that the TestRunner would able to find the steps files.
To achieve that we need to mention the path of the StepDefinition package in CucumberOptions.
(Note: By default Junit/Cucumber finds the test code in the src/test/java folder,
this is why we just need to specify the package name for the cucumber glue).
Updated TestRunner class should look like this:

```
package runners;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"}
)
public class TestRunner {
}
```

6) Run Cucumber test
 - Run as JUnit
 Right Click on TestRunner class and Click Run Test.
 Cucumber will run the script and the result will be shown in the left hand side project explorer window in JUnit tab.
 - Run from Command Prompt
 From IDE Terminal (which sets the location automatically to the root of our project) run the following command:

 ```
 mvn clean compile test
 ```
Out end-to-end test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 2: Page Object Design Pattern with Selenium PageFactory
This section is about Page Object Model Framework which is also known as Page Object Design Pattern or Page Objects.
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
in our end-to-end testing will be represented by a unique class of its own. Such class will inlcude both page element inspection
and associated actions performed by Selenium on the corresponding page.

In order to implement the Page Object Model we will be using **Selenium PageFactory**

Selenium PageFactory is an inbuilt Page Object Model concept for Selenium WebDriver and it is very optimized.
PageFactory is used to Initialise Elements of a Page class without having to use ‘FindElement‘ or ‘FindElements‘.
Annotations can be used to supply descriptive names of target objects to improve code readability.

**@FindBy Annotation:**

As the name suggest, it helps to find the elements in the page using By strategy.
@FindBy can accept TagName, PartialLinkText, Name, LinkText, Id, Css, ClassName, XPath as attributes.
```
@FindBy(id = “idname“)]
public WebElement element;
```
The above code will create a PageObject and name it as 'element' by finding it using its 'id' locator.

**InitElements:**

This Instantiate an Instance of the given class. This method will attempt to instantiate the class given to it,
preferably using a constructor which takes a WebDriver instance as its only argument
An exception will be thrown if the class cannot be instantiated.
```
PageFactory.initElements(WebDriver, PageObject.Class);
```
**Parameters:**

- WebDriver – The driver that will be used to look up the elements

- PageObjects  – A class which will be initialised

**Returns:**
An instantiated instance of the class with WebElement and List<WebElement> fields proxied

**PageFactory NameSpace:**

In order to use PageFactory, *org.openqa.selenium.support.PageFactory* needs to be imported to the associated Class.

## Step 1: Create Checkout Page Object Class
The flow of our test spreads across the following pages:
- Home Page
- Product Listing Page
- Cart Page
- Checkout Page
- Confirmation Page

Therefore, we will be creating corresponding Java Classes in our Project like so
- Personal Details Page
- Shipping Details Page
- Payment Details Page
- Confirmation Details Page

1) Create a New Package file and name it pageObjects, by right click on the src/main/java and select New >> Package.

2) Create five New Class file and name them as was mentioned above
3) Initiate the Page Object for each Class using Constructor
```
public CheckoutPage(WebDriver driver) {
     PageFactory.initElements(driver, this);
}
```
4) Move  WebElement locators associated with a particular page to a corresponding Page Object Class
and replace 'findElement(s) (By by)' method with @FindBy annotation.
5) Wrap Selenium actions performed on each page into re-usable methods and again, place them into corresponding Page Object Class.

(Note: ConfirmationPage.java is the only Class which we leave blank as we have no implementation of @Then Step yet)

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
    HomePage home;
    ProductListingPage productListingPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver","src/drivers/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://www.shop.demoqa.com");
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        home = new HomePage(driver);
        Thread.sleep(1000);
        home.perform_Search("dress");
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
_________________________________________________________________________________________________________________________

# SECTION 3: Page Object Manager
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

The purpose of the Page Object Manger is to create the page’s object and also to make sure that the object
is only created once and it can be used across all step definition files.

## Step 1: Design Page Object Manager Class
1) Create a New Package file and name it 'managers', by right click on the src/main/java and select New >> Package.

2) Create a New Class file and name it PageObjectManager by right click on the above created Package and select New >> Class.
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
This constructor is asking for parameter of type WebDriver.
As to create an object of the Pages, this class requires a driver.

In oder to create an object of PageObjectManager class, driver needs to be provided:
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
Implementation of PageObjectManager requires change in our step definition file as well
Now the duty of the creation of all the pages assigned to only one class which is Page Object Manager.
### Steps.java
```
package stepDefinitions;

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

_________________________________________________________________________________________________________________________

# SECTION 4: Read Project Configurations from Property File
So far in our project we have been storing hard coded values inside the project code.
It is against the coding principles to do so as it makes our test be less manageable and maintainable.
Therefore with the help of properties file we will be focusing on eliminating these hard coded values.

### What is a Property file in Java
**.properties** files are mainly used in Java programs to maintain project **configuration data, database config or
project settings**, etc.
Each parameter in properties file is stored as a pair of strings, in key-value pair format.
You can easily read properties from this file using object of type Properties. This is a utility provided by Java itself.
```
java.util.Properties;
```
### Advantages of Property file in Java
If any information is changed from the properties file, you don’t need to recompile the java class.
In other words, the advantage of using properties file is we can configure things which are prone to change
over a period of time without need of changing anything in code.
## Step 1: Create a Property file
1) Create a New Folder and name it 'configs', by right click on the root Project and select New >> Folder.
2) Create a New File by right click on the above created folder and select New >> File and name it 'Configuration.properties'
3) Write Hard Coded Values in the Property File.

So far there are three hard coded values we will move to our **Configuration.properties** file like so:
```
driverPath=src/drivers/chromedriver
url=http://shop.demoqa.com
implicitWait=5
```
## Step 2: Create a Config File Reader
1) Create a New Package under src/main/java/ and name it 'dataProviders'.
We will keep all the data readers files here in this package.

2) Create a New Class file and name it 'ConfigFileReader', by right click on the above created package and select New >> Class.
3) Add the following code to ConfigFileReader
### ConfigFileReader.java
```
package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "configs//Configuration.properties";


    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getDriverPath(){
        String driverPath = properties.getProperty("driverPath");
        if(driverPath!= null) return driverPath;
        else throw new RuntimeException("driverPath not specified in the Configuration.properties file.");
    }

    public long getImplicitlyWait() {
        String implicitWait = properties.getProperty("implicitWait");
        if(implicitWait != null) return Long.parseLong(implicitWait);
        else throw new RuntimeException("implicitlyWait not specified in the Configuration.properties file.");
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if(url != null) return url;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

}
```
## Explanation
### How to Load Property File
```
BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath));
Properties properties = new Properties();
properties.load(reader);
```

**propertyFilePath :**
This is just a String variable which holds the information of the config file path.

**new FileReader(propertyFilePath) :**
Creates a new FileReader, given the name of the file to read from.

**new BufferedReader(new FileReader(propertyFilePath)) :**
Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters,
arrays, and lines.

**new Properties() :**
The Properties class represents a persistent set of properties. The Properties can be saved to a stream or loaded
from a stream. Each key and its corresponding value in the property list is a string.

**properties.load(reader) :**
Reads a property list (key and value) from the input character stream in a simple line-oriented format.

### ConfigFileReader Method
```
	public String getDriverPath(){
		String driverPath = properties.getProperty("driverPath");
		if(driverPath!= null) return driverPath;
		else throw new RuntimeException("driverPath not specified in the Configuration.properties file.");
	}
  ```

**properties.getProperty(“driverPath”) :**
Properties object gives us a *getProperty()* method which takes the Key of the property as a parameter and return
the Value of the matched key from the .properties file.
If the properties file does not have the specified key, it returns the null.
This is why we have put the null check and in case of null we like to throw an exception to stop the test
with the stack trace information.

## Step 3: Use ConfigFileReader object in the Steps.java file and HomePage.java file
To use the **ConfigFileReader object** in the test, we need to fist create an object of the class.
```
ConfigFileReader configFileReader= new ConfigFileReader();
```


Then we can replace the below statement
```
System.setProperty(“webdriver.chrome.driver”,“scr/drivers”);
```

with
```
System.setProperty(“webdriver.chrome.driver”, configFileReader.getDriverPath());
```
Complete Steps file will look like this now:

### Steps.java
```
package stepDefinitions;

import cucumber.api.java.en.Then;
import dataProviders.ConfigFileReader;
import managers.PageObjectManager;
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
    ConfigFileReader configFileReader;


    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        configFileReader= new ConfigFileReader();
        System.setProperty("webdriver.chrome.driver", configFileReader.getDriverPath());
        driver = new ChromeDriver();
        pageObjectManager = new PageObjectManager(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(configFileReader.getImplicitWait(), TimeUnit.SECONDS);
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
And our Home Page object class file will look like this:

### HomePage.java
```
package pageObjects;

import dataProviders.ConfigFileReader;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    WebDriver driver;
    ConfigFileReader configFileReader;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        configFileReader= new ConfigFileReader();
    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void navigateTo_HomePage() {
        driver.get(configFileReader.getApplicationUrl());
    }

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}

```


Note: Generally, it is bad practice to create object of property file in every class.
We have created the object of the Property Class in Steps file and another object of Properties Class again
in the HomePage class.

We will cover how to overcome this issue in the next section.

Run TestRunner and the test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 5: File Reader Manager as a Singleton Design Pattern
In the previous section, we run into a problem of having multiple instances of Property Class in our project.
In this section we will use File Reader Manager as Singleton Design Pattern to eliminate the issue.
Singleton Design Pattern helps in achieving having only one instance of a class which can be accessed globally.

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
1) Create a New Class and name it as FileReaderManager, by right click on the managers package and select New >> Class.
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
import dataProviders.ConfigFileReader;
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
### HomePage.java
```
package pageObjects;

import dataProviders.ConfigFileReader;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import managers.FileReaderManager;

public class HomePage {

    WebDriver driver;
    ConfigFileReader configFileReader;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        configFileReader= new ConfigFileReader();
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

_________________________________________________________________________________________________________________________

# SECTION 6: WebDriver Manager
Why do we need WebDriver Manager?
Till now we have been creating driver within the Step definition file and explicitly tell our script to start Chrome Driver.
Consequences of that are the following:
- Test script handles the logic of creating WebDriver which is not the best coding practice as code inside test steps
should only be responsible for test execution
- Switching between browsers (e.g. Chrome to Firefox) means changing code for every test containing initialisation of a driver

## Design WebDriver Manager
The only responsibility of the WebDriver Manager is to provide the WebDriver, when we ask for it.
To achieve this we will do the following:

- Specify new WebDriver Properties to the Configuration File
- Create Enums for DriverType and EnvironmentType
- Write new Methods to read the above properties
- Design a WebDriver Manager
- Modify the Steps file to use the new WebDriver Manager in the script

## Step 1 : Add WebDriver Properties to the Configuration file
Configuration file should look like this:
### Configuration.properties
```
environment=local
browser=chrome
windowMaximize=true
driverPath=src/drivers/chromedriver
url=http://shop.demoqa.com
implicitWait=5
```
## Step 2 : Create Enums for DriverType and EnvironmentType
It is always considered a good practice to create enums for all the hard codded values in the project.


1) Create a New Package in src/main/java and name it 'enums'. We will be keeping all the project enums in this package.
2) Create a New Enum and name it 'DriverType' by right click on the above created Package and select New >> Enum.
3) Add one value for Chrome and the other value for FireFox. There can be other browsers as well but lets just take Chrome and Firefox for now.

DriverType file should look like this:
### DriverType.java
```
package enums;
public enum DriverType {
    FIREFOX,
    CHROME
}
```
4) Create another Enum class 'EnvironmentType' and add Local & Remote environmental variables to it.
### EnvironmentType.java
```
package enums;
public enum EnvironmentType {
    LOCAL,
    REMOTE,
}
```
## Step 3 : Write new Method to Read new properties
In ConfigFileReader create **getBrowser(), getEnvironment()** and **getBrowserWindowSize()** methods to read newly added properties

### ConfigFileReader.java
```
package dataProviders;

import enums.DriverType;
import enums.EnvironmentType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "configs/Configuration.properties";


    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            //Configuration properties can be easily read from .properties file using object of type Properties provided by java.utils
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getDriverPath(){
        String driverPath = properties.getProperty("driverPath");
        if(driverPath!= null) return driverPath;
        else throw new RuntimeException("driverPath not specified in the Configuration.properties file.");
    }

    public long getImplicitWait() {
        String implicitWait = properties.getProperty("implicitWait");
        if(implicitWait != null) return Long.parseLong(implicitWait);
        else throw new RuntimeException("implicitlyWait not specified in the Configuration.properties file.");
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if(url != null) return url;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

    public DriverType getBrowser() {
        String browserName = properties.getProperty("browser");
        if(browserName == null || browserName.equals("chrome")) return DriverType.CHROME;
        else if(browserName.equalsIgnoreCase("firefox")) return DriverType.FIREFOX;
        else throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
    }

    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");
        if(environmentName == null || environmentName.equalsIgnoreCase("local")) return EnvironmentType.LOCAL;
        else if(environmentName.equals("remote")) return EnvironmentType.REMOTE;
        else throw new RuntimeException("Environment Type Key value in Configuration.properties is not matched : " + environmentName);
    }

    public Boolean getBrowserWindowSize() {
        String windowSize = properties.getProperty("windowMaximize");
        if(windowSize != null) return Boolean.valueOf(windowSize);
        return true;
    }

}
```
### Explanation
**getBrowserWindowSize() :** Retrieve the property using getProperty method of Properties class.
Null check is performed and in case of null by default value is returned as true. In case of not null,
String value is parsed to Boolean.

**getEnvironment() :** EnvironmentType.Local is returned in case of Null and if the value  is equal to Local.
Which means that in case of missing environment property, execution will be carried on local machine.

**getBrowser() :** Default value is returned as DriverType.Chrome in case of Null. Exception is thrown if
the value does not match with anything.

## Step 4: Design a WebDriver Manager
Now it is the time to design the WebDriver Manager.
The only thing which we need to keep in mind is that the manager would expose only two method for now which are
**getDriver()** and **closeDriver()**.

**GetDriver()** method will decide if the driver is already created or needs to be created.
**GetDriver()** method further call the method

**createDriver()**, which will decide that
the remote driver is needed or local driver for the execution.

Accordingly **CreateDriver()** method would make a call let’s say to **createLocalDriver()**.

**CreateLocalDriver()** method will further decide which type of driver needs to be created.

**closeDriver()** method is responsible for closing the browser and will be called after execution of all tests / test steps is completed
1) Create a new file in src/main/java/managers and call it WebDriverManager
2) Add the following code to it:
### WebDriverManager.java
```
package managers;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import enums.DriverType;
import enums.EnvironmentType;

public class WebDriverManager {
    private WebDriver driver;
    private static DriverType driverType;
    private static EnvironmentType environmentType;
    private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";

    public WebDriverManager() {
        driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
        environmentType = FileReaderManager.getInstance().getConfigReader().getEnvironment();
    }

    public WebDriver getDriver() {
        if(driver == null) driver = createDriver();
        return driver;
    }

    private WebDriver createDriver() {
        switch (environmentType) {
            case LOCAL : driver = createLocalDriver();
                break;
            case REMOTE : driver = createRemoteDriver();
                break;
        }
        return driver;
    }

    private WebDriver createRemoteDriver() {
        throw new RuntimeException("RemoteWebDriver is not yet implemented");
    }

    private WebDriver createLocalDriver() {
        switch (driverType) {
            case FIREFOX : driver = new FirefoxDriver();
                break;
            case CHROME :
                System.setProperty(CHROME_DRIVER_PROPERTY, FileReaderManager.getInstance().getConfigReader().getDriverPath());
                driver = new ChromeDriver();
                break;

        }

        if(FileReaderManager.getInstance().getConfigReader().getBrowserWindowSize()) driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().getConfigReader().getImplicitWait(), TimeUnit.SECONDS);
        return driver;
    }

    public void closeDriver() {
        driver.manage().deleteAllCookies();
        driver.close();
        driver.quit();
    }

}
```
## Step 5: Modify the Steps file to use the new WebDriver Manager in the script
Now we can use WebDriver Manager in our step definition file. After updating the code it should look like this:
### Steps.java
```
package stepDefinitions;

import cucumber.api.java.en.Then;
import managers.PageObjectManager;
import managers.FileReaderManager;
import managers.WebDriverManager;
import org.openqa.selenium.WebDriver;
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
    WebDriverManager webDriverManager;

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver", FileReaderManager.getInstance().getConfigReader().getDriverPath());
        webDriverManager = new WebDriverManager();
        driver = webDriverManager.getDriver();
        pageObjectManager = new PageObjectManager(driver);
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
    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {
        System.out.println("Not implemented");
        webDriverManager.closeDriver();
    }

}
```
Run TestRunner and the test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 7: Sharing Test Context between steps and Step Definition files
## Why do we need to share Test Context?
Dividing Cucumber Steps between many classes may be a good idea. It is, however, probably not needed early in a project.
When you write your first scenario, you will most likely only have just a few steps. The first class with steps is probably
small and you can easily find your way around in it.

But a scenario in Cucumber is a series of steps which gets executed one after one. Each step in scenario may have some state
which can be required by other step in the scenario. In other way you can also say that some steps may depend on
previous steps. This means that we must be able to share state/collected data between steps.

Also when number of tests grows as project matures, keeping all the steps in a single Step Definition class quickly becomes
impractical, so you use many classes.

Now you have a new problem – objects you create in one step class may be needed in the other step classes as well.

In our case as well, till now we just had one scenario which had few steps and we kept all the steps in the same
Step definition file. In a real life project there are tens or even hundreds of scenarios and step defenition files.
And often there is a need to share the Test Context (including Scenario Context, Test State,
data collected during test step execution) with all the Step Definitions files.
Cucumber supports several Dependency Injection (DI) Containers – it simply tells a DI container to instantiate
your step definition classes and wire them up correctly. One of the supported DI containers is **PicoContainer**.

## What is PicoContainer?
**PicoContainer** is a small library which doesn’t require sey up of any configuration and
use of any APIs such as @Inject. It uses constructors instead.

**PicoContainer** really only has a one functionality – it instantiates objects.
Simply hand it some classes and it will instantiate each one, correctly wired together via their constructors.
Cucumber scans your classes with step definitions in them, passes them to PicoContainer,
then asks it to create new instances for every scenario.

We will be performing below steps to share data state across steps:

- Add PicoContainer to the Project
- Create a Test Context class which will hold all the objects state
- Divide the Steps class into multiple steps classes with logical separation
- Write Constructor to share Test Context

## Step 1: Add PicoContainer Library to the Maven Project
```
       <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-picocontainer</artifactId>
            <version>${cucumber-picocontainer.version}</version>
            <scope>test</scope>
        </dependency>
```

Note: It is suggested to use cucumber-picocontainer version same as cucumber. In our case it is 4.2.0

```
    <properties>
        <cucumber.version>4.2.0</cucumber.version>
        <cucumber-picocontainer.version>4.2.0</cucumber-picocontainer.version>
    </properties>

```
## Step 2 : Create a Test Context class
We should create this class logically: identify all information our Step definition file is using
and put that information in to this class. In our case our Step definition file is using the following information:

- PageObjects : Provided by PageObjectManager
- WebDriver : Provided by WebDriverManager
- Properties : Provided by FileReaderManager

So, we need the above objects in our Test Context class.
Next, if we look at the objects, we see that our FileReaderManager is already a Singleton Class and to use it we don’t need
to create an instance of it. It creates its instance by itself. So no need to add FileReaderManager to TestContext class,
as this class can be referred directly statically like
```
FileReaderManager.getInstance()
```
1) Create a New Package under src/main/java and name it 'cucumber'.
We will keep all the Cucumber Helper classes in the same package moving forward.

2) Create a New Class file and name it 'TestContext'.
3) Add the following code to TestContext file:
### TestContext.java
```
package cucumber;

import managers.PageObjectManager;
import managers.WebDriverManager;

public class TestContext {
    private WebDriverManager webDriverManager;
    private PageObjectManager pageObjectManager;

    public TestContext(){
        webDriverManager = new WebDriverManager();
        pageObjectManager = new PageObjectManager(webDriverManager.getDriver());
    }

    public WebDriverManager getWebDriverManager() {
        return webDriverManager;
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

}
```
### Explanation
We kept the initialisation in the constructor and created getMethods() for both the objects.
## Step 3 : Divide the Steps file
We will divide the steps file as we did the separations between the Page Objects - for every different page
we have a separate PageObject class. So it makes sense to have a separate step definition class for every page as well.

1) Create four New Classes in the stepDefinitions package with following names:

- HomePageSteps
- ProductPageSteps
- CartPageSteps
- CheckoutPageSteps

Then we start copying-pasting information from steps class into above created classes accordingly.

(Note: We will create ConfirmationPageSteps Class in the following section and for now leave the code for **@Then** Step in our old
step definition file **Steps.java**)
### HomePageSteps.java
```
package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import managers.PageObjectManager;
import managers.WebDriverManager;
import org.openqa.selenium.WebDriver;
import pageObjects.HomePage;


public class HomePageSteps {
    WebDriver driver;
    HomePage homePage;
    PageObjectManager pageObjectManager;
    WebDriverManager webDriverManager;


    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver", FileReaderManager.getInstance().getConfigReader().getDriverPath());
        webDriverManager = new WebDriverManager();
        driver = webDriverManager.getDriver();
        pageObjectManager = new PageObjectManager(driver);
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
}
```
Code for other Step definition Classes will follow the same principle
## Step 4 : Write Constructor for Step Definition classes to share TestContext
First, lets look at our HomePageSteps file. We need **WebDriverManager** and **PageObjectManager**
in every step file. Therefore we need to create objects for both classes using new operator again and again.

Now with just adding Constructor to HomePageSteps file and passing **TestContext** as a Parameter to constructor
would take all the pain. Within the TestContext object we have everything available which is required for the test.
The new HomePageSteps class should look like this now:
### HomePageSteps.java (after adding step class constructor with TextContext as a parameter)
```
package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import cucumber.TestContext;
import pageObjects.HomePage;


public class HomePageSteps {

    HomePage homePage;
    TestContext testContext;

    //constructor
    public HomePageSteps(TestContext context) {
        testContext = context;
        homePage = testContext.getPageObjectManager().getHomePage();
    }

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        System.setProperty("webdriver.chrome.driver", FileReaderManager.getInstance().getConfigReader().getDriverPath());
        homePage.navigateTo_HomePage();
    }
    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        homePage.perform_Search("dress");
        Thread.sleep(1000);
    }
}

```
Using PicoContainer to share state between steps in a scenario is easy and non intrusive.
All we need is a constructor that requires an object that PicoContainer can create and inject.
PicoContainer is invisible. Add cucumber-picocontainer dependency and make sure that the constructors
for the step classes require an instance of a the same class.

Now we can start modifying rest of the step classes:
## ProductPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import pageObjects.ProductListingPage;

public class ProductPageSteps {

    TestContext testContext;
    ProductListingPage productListingPage;

    public ProductPageSteps(TestContext context) {
        testContext = context;
        productListingPage = testContext.getPageObjectManager().getProductListingPage();
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() throws InterruptedException {
        Thread.sleep(1000);
        productListingPage.select_Product(0);
        productListingPage.makeSelection(1);
        productListingPage.clickOn_AddToCart();
    }
}
```
## CartPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import pageObjects.CartPage;

public class CartPageSteps {

    TestContext testContext;
    CartPage cartPage;

    public CartPageSteps(TestContext context) {
        testContext = context;
        cartPage = testContext.getPageObjectManager().getCartPage();
    }
    @When("I move to checkout from mini cart")
    public void i_move_to_checkout_from_mini_cart() throws InterruptedException{
        Thread.sleep(1000);
        cartPage.clickOn_Cart();
        cartPage.clickOn_ContinueToCheckout();
    }
}
```
## CheckoutPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import pageObjects.CheckoutPage;

public class CheckoutPageSteps {
    TestContext testContext;
    CheckoutPage checkoutPage;

    public CheckoutPageSteps(TestContext context) {
        testContext = context;
        checkoutPage = testContext.getPageObjectManager().getCheckoutPage();
    }

    @When("I enter my personal details")
    public void i_enter_my_personal_details() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.fill_PersonalDetails();

    }

    @When("I place the order")
    public void i_place_the_order() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.check_TermsAndCondition();
        checkoutPage.clickOn_PlaceOrder();
        testContext.getWebDriverManager().closeDriver();
    }

}
```
## Steps.java
```
package stepDefinitions;

import cucumber.api.java.en.Then;

public class Steps {

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {
        System.out.println("Not implemented");

    }

}
```

Run TestRunner and the test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 8: Hooks in Cucumber Framework
Unlike **TestNG Annotations**, Cucumber supports only two hooks (**@Before** and  **@After**).
They work at the start and the end of the test scenario. As the name suggests, @Before hook gets executed well before
any other test scenario, and @After hook gets executed after executing the scenario.

Implementation of Cucumber Hooks will allow us to to move manipulations with WebDriver (initialisation / closing down)
from Page Objects and Step definitions to Hooks Class.
In order to achieve the above, we will be performing below steps:

- Create a Hook Class
- Add goToUrl() method to Web Driver Manager Class
- Remove WebDriverManager and FileReaderManager related code from HopePage Class
- Modify HomePageSteps definition file to reflect the changes
- Remove closeDriver() method from CheckoutPageSteps definition file


## Step 1: Create a Hooks Class
Create a New Class file in src/test/java inside stepDefinitions package and name it 'Hooks' and place the following code inside it
### Hooks.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();

    }

    @After
    public void AfterSteps() {
        testContext.getWebDriverManager().closeDriver();
    }

}
```
## Step 2: Add goToUrl() method to Web Driver Manager Class
Add a new method to WebDriverManager.java. The method will take
App URL as a parameter and execute Selenium driver.get("Url") method.
It will be called from HomePageSteps definition file where ConfigReader will read App URL from our
Configuration.properties file
```
    public void goToUrl(String url) {
        driver.get(url);
    }
```
## Step 3: Remove WebDriverManager and FileReaderManager related code from HopePage Class
HopePage Class should look like this:
### HomePage.java
```
package pageObjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}
```
## Step 4: Modify HomePageSteps definition file to reflect the changes
HopePageSteps definition file should look like this:
### HopePageSteps.java
```
package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import cucumber.TestContext;
import pageObjects.HomePage;

public class HomePageSteps {
    HomePage homePage;
    TestContext testContext;

    //constructor
    public HomePageSteps(TestContext context) {
        testContext = context;
        homePage = testContext.getPageObjectManager().getHomePage();
    }

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        testContext.getWebDriverManager().goToUrl(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() throws InterruptedException {
        homePage.perform_Search("dress");
        Thread.sleep(1000);

    }
}
```
## Step 5: Remove closeDriver() method from CheckoutPageSteps definition file
CheckoutPageSteps definition file should look like this;
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import pageObjects.CheckoutPage;

public class CheckoutPageSteps {
    TestContext testContext;
    CheckoutPage checkoutPage;

    public CheckoutPageSteps(TestContext context) {
        testContext = context;
        checkoutPage = testContext.getPageObjectManager().getCheckoutPage();
    }

    @When("I enter my personal details")
    public void i_enter_my_personal_details() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.fill_PersonalDetails();
    }

    @When("I place the order")
    public void i_place_the_order() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.check_TermsAndCondition();
        checkoutPage.clickOn_PlaceOrder();
    }
}
```
Run TestRunner and the test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 9: Data tables in Cucumber 3 +
Version 3 and above of Cucumber brings a new implementation of Data tables.
From a Gherkin perspective, nothing has changed. Data tables are supported as earlier. However, implementation needs some
adaptation.
In our project we will be converting **N-column data table into a custom type**

Note: For more information about different DataTables data structures and conversions please follow the below links:
- https://github.com/cucumber/cucumber/tree/master/datatable
- http://www.thinkcode.se/blog/2018/06/30/data-tables-in-cucumber-3
- https://github.com/grasshopper7/cuke3-migrate-datatabletype/blob/master/cuke3-migrate-datatabletype/src/test/java/dataobject/Lecture.java

In order to implement DataTable we will be performing the below steps:
- Change End2End_Test.feature file so Customer details are specified in a DataTable
- Implement new (custom) type which represents DataTable data
- Register a newly created customer type so Cucumber can convert the data table to it
- In CheckoutPage Class, replace fill_PersonalDetails() method with a new method which reads data from DataTable
- Modify Step definition file to reflect DataTable implementation

## Step 1: Change End2End_Test.feature file so Customer details are specified in a DataTable
Our feature file should now look like this:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Scenario: Customer place an order by purchasing an item from search
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart
    And I enter my personal details as follows
      |  first_name  |last_name|     country        |     street_address     |          city     |postcode|phone_number|email_address|
      |TestAutomation| Opencast| United Kingdom (UK)|Hoults Yard, Walker Road|Newcastle upon Tyne|NE6 3PE |07438862327 |test@test.com|
    And place the order
    Then Order details are successfully verified
```
## Step 2: Implement new (custom) type which represents DataTable data
1) Create a new package in src/main/java and name it 'testDataTypes'
2) Create a New Class file in src/main/java inside testDataTypes package and name it 'CustomerDataType'.
3) The new type CustomerDataType has to be implemented. This is one possible implementation:
### CustomerDataType.java
```
package testDataTypes;

import java.util.Map;

public class CustomerDataType {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String streetAddress;
    private String city;
    private String postCode;
    private String country;
    private String phoneNumber;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public static CustomerDataType customerDetails (Map<String, String> entry) {
        CustomerDataType input = new CustomerDataType();
        input.setFirstName(entry.get("first_name"));
        input.setLastName(entry.get("last_name"));
        input.setCountry(entry.get("country"));
        input.setStreetAddress(entry.get("street_address"));
        input.setCity(entry.get("city"));
        input.setPostCode(entry.get("postcode"));
        input.setPhoneNumber(entry.get("phone_number"));
        input.setEmailAddress(entry.get("email_address"));

        return input;
    }

    @Override
    public String toString() {
        return "CustomerDataType [" +
                "first_name: " + firstName +
                ", last_name: " + lastName +
                ", country: " + country +
                ", street_address:" + streetAddress +
                ", city: " + city +
                ", postcode: " + postCode +
                ", phone_number: " + phoneNumber +
                ", email_address: " + emailAddress +
                "]";
    }
}
```
It is an immutable type with the same number of fields as columns in our DataTable and
hhe fields match the table headers. The fields and headers doesn't have to match. But as they describe the same thing
it feels natural that they have the same name in this case.
The next step is new for Cucumber 3 +. The type has to registered before it can be used in a data table:
## Step 3: Register a newly created customer type so Cucumber can convert the data table to it
1) Create a new class in src/test/java inside stepDefinitions package and name it 'Configurer'
2) Place the following code inside it:
```
package stepDefinitions;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import java.util.Locale;
import java.util.Map;
import testDataTypes.*;

public class Configurer implements TypeRegistryConfigurer {
    @Override
    public void configureTypeRegistry(TypeRegistry registry) {

        registry.defineDataTableType(new DataTableType(CustomerDataType.class, new TableEntryTransformer<CustomerDataType>() {
            @Override
            public CustomerDataType transform(Map<String, String> entry) {
                return CustomerDataType.customerDetails(entry);
            }
        }));
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }
}
```
With this infrastructure prepared, it is time to do the actual implementation of the steps that uses this data table:
## Step 4: In CheckoutPage Class, replace fill_PersonalDetails() method with a new method which reads data from DataTable
CheckoutPage should look like this:
```
package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import testDataTypes.CustomerDataType;
import java.util.ArrayList;
import java.util.List;

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

    public void CustomerPersonalDetailsFromDataTable(List<CustomerDataType> inputs) {
        try {
            //Creating arrays for each table column header
            List<String> firstNameArr = new ArrayList<>();
            List<String> lastNameArr = new ArrayList<>();
            List<String> countryArr = new ArrayList<>();
            List<String> streetAddressArr = new ArrayList<>();
            List<String> cityArr = new ArrayList<>();
            List<String> postcodeArr = new ArrayList<>();
            List<String> phoneNumberArr = new ArrayList<>();
            List<String> emailAddressArr = new ArrayList<>();
            Integer size = inputs.size();
            //Recording data from table rows into corresponding data arrays
            for (CustomerDataType input : inputs) {
                firstNameArr.add(input.getFirstName());
                lastNameArr.add(input.getLastName());
                countryArr.add(input.getCountry());
                streetAddressArr.add(input.getStreetAddress());
                cityArr.add(input.getCity());
                postcodeArr.add(input.getPostCode());
                phoneNumberArr.add(input.getPhoneNumber());
                emailAddressArr.add(input.getEmailAddress());
            }
            //Assigning DataTable data to corresponding variables. We use these values in .sendKey() method
            for (int i = 0; i < size; i++) {
                String firstNameKey = firstNameArr.get(i);
                String lastNameKey = lastNameArr.get(i);
                String countryKey = countryArr.get(i);
                String streetAddressKey = streetAddressArr.get(i);
                String cityKey = cityArr.get(i);
                String postcodeKey = postcodeArr.get(i);
                String phoneNumberKey = phoneNumberArr.get(i);
                String emailAddressKey = emailAddressArr.get(i);

                txtbx_FirstName.sendKeys(firstNameKey);
                txtbx_LastName.sendKeys(lastNameKey);
                select_Country(countryKey);
                txtbx_Address.sendKeys(streetAddressKey);
                txtbx_City.sendKeys(cityKey);
                txtbx_PostCode.sendKeys(postcodeKey);
                Thread.sleep(2000);
                txtbx_Phone.sendKeys(phoneNumberKey);
                txtbx_Email.sendKeys(emailAddressKey);
            }
        } catch (Exception e) {
            Assert.fail("Unable to to locate WebElement or/and send keys to it, Exception: " + e.getMessage());
        }
    }
}
```

## Step 5: Modify Step definition file to use new method
1) Run TestRunner to get our modified step code snippet:
```
@When("I enter my personal details as follows")
public void i_enter_my_personal_details_as_follows(io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new cucumber.api.PendingException();
}
```
2) Inside the step body include **CustomerPersonalDetailsFromDataTable(List<CustomerDataType> inputs)** method invocation.
CheckoutPageSteps should look look this:
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import pageObjects.CheckoutPage;
import testDataTypes.CustomerDataType;
import java.util.List;

public class CheckoutPageSteps {
    TestContext testContext;
    CheckoutPage checkoutPage;

    public CheckoutPageSteps(TestContext context) {
        testContext = context;
        checkoutPage = testContext.getPageObjectManager().getCheckoutPage();
    }

    @When("I enter my personal details as follows")
    public void i_enter_my_personal_details_as_follows(List<CustomerDataType> inputs) throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.CustomerPersonalDetailsFromDataTable(inputs);

    }

    @When("I place the order")
    public void i_place_the_order() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.check_TermsAndCondition();
        checkoutPage.clickOn_PlaceOrder();

    }
}
```
Run TestRunner and test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 10: Implicit and Explicit Wait in Selenium WebDriver

In selenium "Waits" play an important role in executing tests.
## Why Do We Need Waits In Selenium?
Most of the web applications are developed using Ajax and Javascript.
When a page is loaded by the browser the elements which we want to interact with may load at different time intervals.
Not only it makes this difficult to identify the element but also if the element is not located it will throw an
**"ElementNotVisibleException"** exception. Using Waits, we can resolve this problem.
### Selenium Web Driver Waits
- Implicit Wait
- Explicit Wait

### Implicit Wait
Selenium Web Driver has borrowed the idea of implicit waits from **Watir**.

The implicit wait will tell to the web driver to wait for certain amount of time before it throws a
**"No Such Element Exception"**.
The default setting is 0. Once we set the time,
web driver will wait for that time before throwing an exception.
We have **Implicit Wait** implementation in our project inside WebDriver Manager class when we are waiting for the App URL to load:
```
driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().getConfigReader().getImplicitWait(), TimeUnit.SECONDS);
```

### Explicit Wait
The explicit wait is used to tell the Web Driver to wait for certain conditions (Expected Conditions) or the maximum time
exceeded before throwing an "ElementNotVisibleException" exception.
The explicit wait is an intelligent kind of wait, but it can be applied **only for specified elements**.
Explicit wait gives better options than that of an implicit wait as it will wait for dynamically loaded Ajax elements.
Once we declare explicit wait we have to use **"ExpectedConditions"**.

So far in our project we are using **Thread.Sleep()**. This generally is not recommended to use as it significantly
slows down test execution. Execution will just stops for specified in Thread.sleep()statement time before continue with
the next statement.
In order to make our test execution more time efficient, we are going to implement a couple of
**Explicit Wait** and custom specified timeout time.
In order to implement Explicit Wait we will be performing the below steps:
- Create New utility class where all Wait methods will be placed
- Add explicitWait variable into Configuration.properties file
- Modify getImplicitWait method so it reads both custom Explicit and Implicit Wait value by taken a 'Key' as a parameter
- Include Wait methods inside our project's PageObject classes
- Modify Step definition files to reflect the changes
- Modify createLocalDriver() method inside WebDriver Manager to add implicitWait parameter to it

## Step 1: Create New utility class where all Wait methods will be placed
1) Create a new package in src/main/java and name it 'utils'
2) Create a New Class file in src/main/java inside 'utils' package and name it 'Waits'.
We will add two Explicit Wait methods to our Wait Class:
- ExpectedConditions.elementToBeClickable(element))
- ExpectedConditions.visibilityOf(element)
New Waits Class should look like this:
### Waits.java
```
package utils;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waits {

    public boolean WaitForClickableWithCustomTimeout(WebDriver driver, WebElement element, long customTimeout) {

        try {

            final WebDriverWait customWait;
            customWait= new WebDriverWait(driver, 10);
            customWait.until(ExpectedConditions.elementToBeClickable(element));
            System.out.println("Element is clickable, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            return true;

        } catch (Exception e) {
            System.out.println("Unable to click on WebElement, locator: " + "<" + element.getClass() + ">" + ", custom Timeout: " + customTimeout);
            Assert.fail("Unable to click on WebElement, Exception: " + e.getMessage());
            return false;
        }
    }
    public boolean WaitForVisibleWithCustomTimeout(WebDriver driver, WebElement element, long customTimeout) {

        try {

            final WebDriverWait customWait;
            customWait= new WebDriverWait(driver, customTimeout);
            customWait.until(ExpectedConditions.visibilityOf(element));
            System.out.println("Successfully found WebElement, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            return true;

        } catch (Exception e) {
            System.out.println("Unable to find WebElement, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            Assert.fail("Unable to find WebElement, Exception: " + e.getMessage());
            return false;
        }
    }
 }

```
## Step 2: Add explicitWait variable into Configuration.properties file
Our **Configuration.properties** file should look like this:
```
environment=local
browser=chrome
windowMaximize=true
driverPath=src/drivers/chromedriver
url=http://shop.demoqa.com
implicitWait=10
explicitWait=5
```
## Step 3: Modify getImplicitWait method
Inside scr/main/java/dataProviders/ConfigFileReader we will modify our getImplicitWait() method so it can read both custom Implicit and Explicit Wait values.
We give it a more meaningful name too:
```
    public long getCustomWait(String waitTypeKey) {
        String customWait = properties.getProperty(waitTypeKey);
        if(customWait != null) return Long.parseLong(customWait);
        else throw new RuntimeException("Custom Wait not specified in the Configuration.properties file.");
    }
```
### ConfigFileReader.java
```
package dataProviders;

import enums.DriverType;
import enums.EnvironmentType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "configs/Configuration.properties";


    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            //Configuration properties can be easily read from .properties file using object of type Properties provided by java.utils
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getDriverPath(){
        String driverPath = properties.getProperty("driverPath");
        if(driverPath!= null) return driverPath;
        else throw new RuntimeException("driverPath not specified in the Configuration.properties file.");
    }

    public long getCustomWait(String waitTypeKey) {
        String customWait = properties.getProperty(waitTypeKey);
        if(customWait != null) return Long.parseLong(customWait);
        else throw new RuntimeException("Custom Wait not specified in the Configuration.properties file.");
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if(url != null) return url;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

    public DriverType getBrowser() {
        String browserName = properties.getProperty("browser");
        if(browserName == null || browserName.equals("chrome")) return DriverType.CHROME;
        else if(browserName.equalsIgnoreCase("firefox")) return DriverType.FIREFOX;
        else throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
    }

    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");
        if(environmentName == null || environmentName.equalsIgnoreCase("local")) return EnvironmentType.LOCAL;
        else if(environmentName.equals("remote")) return EnvironmentType.REMOTE;
        else throw new RuntimeException("Environment Type Key value in Configuration.properties is not matched : " + environmentName);
    }

    public Boolean getBrowserWindowSize() {
        String windowSize = properties.getProperty("windowMaximize");
        if(windowSize != null) return Boolean.valueOf(windowSize);
        return true;
    }

}
```
## Step 4: Include Wait methods inside our project's PageObject classes
### HomePage.java
```
package pageObjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Waits;

public class HomePage {
    WebDriver driver;
    Waits wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);
    }
    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void perform_Search(String search, long customTimeout) {
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_Search, customTimeout)) {
            btn_Search.click();
            input_Search.sendKeys(search);
            input_Search.sendKeys(Keys.RETURN);
        }
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
import utils.Waits;

public class ProductListingPage {
    WebDriver driver;
    Waits wait;

    public ProductListingPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
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


    public void select_Product(int productNumber, long customTimeout) {

        if (wait.WaitForVisibleWithCustomTimeout(driver,prd_List.get(productNumber), customTimeout)) {
            prd_List.get(productNumber).click();
        }
    }

    public void makeSelection(int index, long customTimeout) {
        if (wait.WaitForVisibleWithCustomTimeout(driver,selectColour, customTimeout)) {
            Select colour = new Select(selectColour);
            colour.selectByIndex(index);
            Select size = new Select(selectSize);
            size.selectByIndex(index);
        }
    }

    public void clickOn_AddToCart(long customTimeout) {

        if (wait.WaitForClickableWithCustomTimeout(driver,btn_AddToCart, customTimeout)) {
            btn_AddToCart.click();
        }
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
import utils.Waits;

public class CartPage {

    WebDriver driver;
    Waits wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".cart-button")
    public WebElement btn_Cart;

    @FindBy(css = ".checkout-button.alt")
    public WebElement btn_ContinueToCheckout;


    public void clickOn_Cart(long customTimeout) {
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_Cart, customTimeout)) {
            btn_Cart.click();
        }
    }

    public void clickOn_ContinueToCheckout(long customTimeout){
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_ContinueToCheckout, customTimeout)) {
            btn_ContinueToCheckout.click();
        }
    }
}
```
### CheckoutPage.java
```
package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import testDataTypes.CustomerDataType;
import utils.Waits;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPage {
    WebDriver driver;
    Waits wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
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

    @FindBy(id = "order_review")
    public WebElement test;


    public void select_Country(String countryName) {
            Select country = new Select(select_Country);
            country.selectByVisibleText(countryName);
     }

    public void check_TermsAndCondition(long customTimeout) {
        if(wait.WaitForClickableWithCustomTimeout(driver,chkbx_AcceptTermsAndCondition, customTimeout)) {
            chkbx_AcceptTermsAndCondition.click();
        }
    }

    public void clickOn_PlaceOrder(long customTimeout) {
        if(wait.WaitForClickableWithCustomTimeout(driver,chkbx_AcceptTermsAndCondition, customTimeout)) {
            btn_PlaceOrder.submit();
        }
    }

    public void CustomerPersonalDetailsFromDataTable(List<CustomerDataType> inputs, long customTimeout) {
        try {
            //Creating arrays for each table column header
            List<String> firstNameArr = new ArrayList<>();
            List<String> lastNameArr = new ArrayList<>();
            List<String> countryArr = new ArrayList<>();
            List<String> streetAddressArr = new ArrayList<>();
            List<String> cityArr = new ArrayList<>();
            List<String> postcodeArr = new ArrayList<>();
            List<String> phoneNumberArr = new ArrayList<>();
            List<String> emailAddressArr = new ArrayList<>();
            Integer size = inputs.size();
            //Recording data from table rows into corresponding data arrays
            for (CustomerDataType input : inputs) {
                firstNameArr.add(input.getFirstName());
                lastNameArr.add(input.getLastName());
                countryArr.add(input.getCountry());
                streetAddressArr.add(input.getStreetAddress());
                cityArr.add(input.getCity());
                postcodeArr.add(input.getPostCode());
                phoneNumberArr.add(input.getPhoneNumber());
                emailAddressArr.add(input.getEmailAddress());
            }
            //Assigning DataTable data to corresponding variables. We use these values in .sendKey() method
            for (int i = 0; i < size; i++) {
                String firstNameKey = firstNameArr.get(i);
                String lastNameKey = lastNameArr.get(i);
                String countryKey = countryArr.get(i);
                String streetAddressKey = streetAddressArr.get(i);
                String cityKey = cityArr.get(i);
                String postcodeKey = postcodeArr.get(i);
                String phoneNumberKey = phoneNumberArr.get(i);
                String emailAddressKey = emailAddressArr.get(i);

                if(wait.WaitForVisibleWithCustomTimeout(driver,test, customTimeout)) {

                    txtbx_FirstName.sendKeys(firstNameKey);
                    txtbx_LastName.sendKeys(lastNameKey);
                    select_Country(countryKey);
                    txtbx_Address.sendKeys(streetAddressKey);
                    txtbx_City.sendKeys(cityKey);
                    txtbx_PostCode.sendKeys(postcodeKey);
                    Thread.sleep(2000);
                    txtbx_Phone.sendKeys(phoneNumberKey);
                    txtbx_Email.sendKeys(emailAddressKey);
                }
            }

        } catch (Exception e) {
            Assert.fail("Unable to to locate WebElement or/and send keys to it, Exception: " + e.getMessage());
        }
    }
}
```
##  Step 5: Modify Step definition files to reflect the changes
### HomePageSteps.java
```
package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import cucumber.TestContext;
import pageObjects.HomePage;

public class HomePageSteps {

    HomePage homePage;
    TestContext testContext;
    String url = FileReaderManager.getInstance().getConfigReader().getApplicationUrl();
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    //constructor
    public HomePageSteps(TestContext context) {
        testContext = context;
        homePage = testContext.getPageObjectManager().getHomePage();
    }

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        testContext.getWebDriverManager().goToUrl(url);
    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() {
        homePage.perform_Search("dress", customTimeout);
    }
}

```
### ProductPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import pageObjects.ProductListingPage;

public class ProductPageSteps {

    TestContext testContext;
    ProductListingPage productListingPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public ProductPageSteps(TestContext context) {
        testContext = context;
        productListingPage = testContext.getPageObjectManager().getProductListingPage();
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() throws InterruptedException {
        productListingPage.select_Product(0, customTimeout);
        productListingPage.makeSelection(1, customTimeout);
        productListingPage.clickOn_AddToCart(customTimeout);
    }
}

```
### CartPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import pageObjects.CartPage;

public class CartPageSteps {

    TestContext testContext;
    CartPage cartPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public CartPageSteps(TestContext context) {
        testContext = context;
        cartPage = testContext.getPageObjectManager().getCartPage();
    }
    @When("I move to checkout from mini cart")
    public void i_move_to_checkout_from_mini_cart() throws InterruptedException{
        Thread.sleep(1000);
        cartPage.clickOn_Cart(customTimeout);
        cartPage.clickOn_ContinueToCheckout(customTimeout);
    }
}
```
### CheckoutPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import pageObjects.CheckoutPage;
import testDataTypes.CustomerDataType;
import java.util.List;

public class CheckoutPageSteps {
    TestContext testContext;
    CheckoutPage checkoutPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public CheckoutPageSteps(TestContext context) {
        testContext = context;
        checkoutPage = testContext.getPageObjectManager().getCheckoutPage();
    }

    @When("I enter my personal details as follows")
    public void i_enter_my_personal_details_as_follows(List<CustomerDataType> inputs) {
        checkoutPage.CustomerPersonalDetailsFromDataTable(inputs, customTimeout);

    }

    @When("I place the order")
    public void i_place_the_order() {
        checkoutPage.check_TermsAndCondition(customTimeout);
        checkoutPage.clickOn_PlaceOrder(customTimeout);

    }
}
```
## Step 6: Modify createLocalDriver() method inside WebDriver Manager class to add implicitWait parameter to it
```
private WebDriver createLocalDriver() {
        switch (driverType) {
            case FIREFOX : driver = new FirefoxDriver();
                break;
            case CHROME :
                System.setProperty(CHROME_DRIVER_PROPERTY, FileReaderManager.getInstance().getConfigReader().getDriverPath());
                driver = new ChromeDriver();
                break;

        }

        if(FileReaderManager.getInstance().getConfigReader().getBrowserWindowSize()) driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().getConfigReader().getCustomWait("implicitWait"), TimeUnit.SECONDS);
        return driver;
    }
```
Run TestRunner and test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 11: Share data between steps in Cucumber using Scenario Context
This section is about how we can share Test Context between the Cucumber Steps.

During test scenario execution there might be situations where we need to carry a data or a state from one step to another.

This data or state can be within these two levels:

- Test Context
- Scenario Context
So what is Test Context and Scenario Context and what is the difference between two.

### Test Context
TestContext is the parent class and the medium to share the information between the different steps in a test.

It can have many class objects in it. If we go back to our previous tutorial of Test Context, we can see
that it already has **PageObjectManager** and **WebDriverManager** object in it.
### Scenario Context
Scenario Context is a class which holds the test data information specifically.
It actually use the Test Context to travel the information between various steps.
With in this ScenarioContext class, we can create any number of fields to store any form of data.
It stores the information in the key value pair and again, value can be of any type.
It can store String, Boolean, Integer or may be a Class. Also the important point here is that the information
which we store in Scenario Context is generated at the run time. This means that during the execution, if you wish
to store some information, you will use Scenario Context.

Structure of the TestContext class will be like this:
```
public class TestContext {
 WebDriverManager webDriverManager;
 PageObjectManager pageObjectManager;
 ScenarioContext scenarioContext;
}
```
## Share data between steps in Cucumber using Scenario Context
Let’s get back to our Test Scenario and implement a validation.
Lets say we want to validate that the Name of the Product displayed on the order confirmation page
is the same as for the Product we added to the cart.
In that case we need to store the name of the product in Scenario Context object at the adding product to the cart step.
We will retrieve the product from ScenarioContext and validate the name on the final confirmation page
after the order placement is done.

## Step 1 : Design a Scenario Context class
1) Create a new enum in src/main/java inside 'enums' package and name it 'Context'.
```
package enums;

public enum Context {
    PRODUCT_NAME;
}
```
2) Create a new class in src/main/java inside 'testDataTypes package and name it 'ScenarioContext'
## ScenarioContext.java
```
package testDataTypes;

import java.util.HashMap;
import java.util.Map;
import enums.Context;

public class ScenarioContext {

    private  Map<String, Object> scenarioContext;

    public ScenarioContext(){
        scenarioContext = new HashMap<>();
    }

    public void setContext(Context key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public Object getContext(Context key){
        return scenarioContext.get(key.toString());
    }

    public Boolean isContains(Context key){
        return scenarioContext.containsKey(key.toString());
    }

}
```
### Explanation

**scenarioContext :**
This is a HasMap object which store the information in the Key-Value pair.
Key type is String and Value can be of any Object Type.

**setContext() :**
This method takes two parameters,  key as String and value as object. Key is nothing but a Context enum.

**getContext() :** This method takes key as parameter and returned the object which match the key.

**isContains() :** This method performs a check on the complete Map that if it contains the key or not.

3) Include **ScenarioContext** in **TextContext**, so that it can be shared across all the Cucumber Steps
using Pico-Container library. Also, we need to add a getter method as **getScenarioContext()** to get the
scenarioContext object.
### TestContext.java
```
package cucumber;

import managers.PageObjectManager;
import managers.WebDriverManager;
import testDataTypes.ScenarioContext;

public class TestContext {
    private WebDriverManager webDriverManager;
    private PageObjectManager pageObjectManager;
    public ScenarioContext scenarioContext;

    public TestContext(){
        webDriverManager = new WebDriverManager();
        pageObjectManager = new PageObjectManager(webDriverManager.getDriver());
        scenarioContext = new ScenarioContext();
    }

    public WebDriverManager getWebDriverManager() {
        return webDriverManager;
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

}
```
## Step 2 : Save test information/data/state in the Scenario Context
To use the value of the product name later in the test for validation, we need to save its Name as a part of
**'I choose to buy the first item**' step.

1) Add a new **getProductName()** method in the ProductListingPage class which will return the Name of the Product.
### ProductListingPage.java
```
package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import utils.Waits;

public class ProductListingPage {
    WebDriver driver;
    Waits wait;

    public ProductListingPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
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

    @FindBy(css= ".entry-summary")
    public WebElement selectedProduct;


    public void select_Product(int productNumber, long customTimeout) {

        if (wait.WaitForVisibleWithCustomTimeout(driver,prd_List.get(productNumber), customTimeout)) {
            prd_List.get(productNumber).click();
        }
    }

    public void makeSelection(int index, long customTimeout) {
        if (wait.WaitForVisibleWithCustomTimeout(driver,selectColour, customTimeout)) {
            Select colour = new Select(selectColour);
            colour.selectByIndex(index);
            Select size = new Select(selectSize);
            size.selectByIndex(index);
        }
    }

    public void clickOn_AddToCart(long customTimeout) {

        if (wait.WaitForClickableWithCustomTimeout(driver,btn_AddToCart, customTimeout)) {
            btn_AddToCart.click();
        }
    }

    public String getProductName(long customTimeout) {
        String productName;
        if (wait.WaitForVisibleWithCustomTimeout(driver,selectedProduct, customTimeout)) {
            productName = selectedProduct.findElement(By.cssSelector("h1")).getText();
        } else {
            productName = "Unable to get Product Name";
        }
        return productName;
    }

}

```
2) Now, using the **getProductName()** method, get the name and save it into the scenarioContext object in
ProductPageSteps class
### ProductPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import enums.Context;
import managers.FileReaderManager;
import pageObjects.ProductListingPage;

public class ProductPageSteps {

    TestContext testContext;
    ProductListingPage productListingPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public ProductPageSteps(TestContext context) {
        testContext = context;
        productListingPage = testContext.getPageObjectManager().getProductListingPage();
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() {
        productListingPage.select_Product(0, customTimeout);
        String productName = productListingPage.getProductName(customTimeout);
        testContext.scenarioContext.setContext(Context.PRODUCT_NAME, productName);
        productListingPage.makeSelection(1, customTimeout );
        productListingPage.clickOn_AddToCart(customTimeout);
    }
}
```
## Step 3: Implement Product Name Validation (@Then step)
1) Add the following code to ConfirmationPage Class:
```
package pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Waits;


public class ConfirmationPage {
    WebDriver driver;
    Waits wait;

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);
    }

    @FindAll(@FindBy(css = ".order_item"))
    public List<WebElement> prd_List;

    @FindBy(css = ".woocommerce-order-details")
    public WebElement orderDetails;


    public List<String> getProductNames(long customTimeout) {
        List<String> productNames = new ArrayList<>();
        if (wait.WaitForVisibleWithCustomTimeout(driver, orderDetails, customTimeout)) {
            for (WebElement element : prd_List) {
                productNames.add(element.findElement(By.cssSelector(".product-name")).getText());
            }
        }
        return productNames;
    }
}

```
2) Add a new **getConfirmationPage()** method to get the Confirmation Page object in the PageObjectManager class.
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
    public ConfirmationPage getConfirmationPage() {
        return (confirmationPage == null) ? confirmationPage = new ConfirmationPage(driver) : confirmationPage;
    }
}
```

3) Now we can finally move our code for Confirmation page step from the old Steps class file to ConfirmationPageSteps class
### ConfirmationPageSteps.java
```
package stepDefinitions;

import managers.FileReaderManager;
import org.junit.Assert;
import cucumber.TestContext;
import cucumber.api.java.en.Then;
import enums.Context;
import pageObjects.ConfirmationPage;


public class ConfirmationPageSteps {
    TestContext testContext;
    ConfirmationPage confirmationPage;
    public long customTimeout;

    public ConfirmationPageSteps(TestContext context) {
        testContext = context;
        confirmationPage = testContext.getPageObjectManager().getConfirmationPage();
        customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {

        String productName = ((String) testContext.scenarioContext.getContext(Context.PRODUCT_NAME)).toLowerCase();
        Assert.assertTrue(confirmationPage.getProductNames(customTimeout).stream().filter(x -> x.contains(productName)).findFirst().get().length() > 0);

    }
}
```
4) Delete Steps.java file from scr/test/java/stepDefinitions package

Run TestRunner and test should be executed successfully

_________________________________________________________________________________________________________________________

# SECTION 12: Data Driven Testing using Json with Cucumber
## What is Data Driven Testing?
Data that is external to functional tests is loaded and used to extend automated test cases.
In order to demonstrate DDT in practice we will use entering customer personal details step.
We currently use data table to read the data from. This is also a part of DDT, however the data is specified directly in our
feature file. This is perfectly fine, but what if in real word project we need to use large data sample or apply different data sets to the same test scenario?
To be sure that the application works as expected with different sets of input data and for more effective management of test
data DDT comes into play.

### How Data Driven Testing can be done with Cucumber?
There a few different ways of doing Data Driven Testing with Cucumber:

- Parameterization in Cucumber
- reading data from external file using Examples Keyword
- Data Tables in Cucumber

Out of above, we will use the Data Driven Technique using Example Keywords in our below example.
And we will be using JSON to provide Data to our test.

## What is JSON?
JSON is short for JavaScript Object Notation, and is a way to store information in an organized, easy-to-access manner.
It gives us a human-readable collection of data that we can access in a really logical manner.

### Why JSON over Excel?
Excel is good to manage data and to use but it comes with its own limitations. Like MS Office needs to be installed
on the system where the tests are being executed. This is a big limitation on its own, as the test servers has never
bound to have have such dependencies. If test are meant to run on Mac, then it is also a problem.

We have to do lot of amendments in our project in this chapter to implement Data Driven Technique using JSON files:

- Add another senario which will be using JSON file as a data source
- Create JSON Data set
- Write a Java POJO class to represent JSON data
- Pass JSON data file location to Properties file and Write a method to read the same
- Create a JSON Data Reader class
- Modify FileReaderManager to accommodate JSON Data Reader
- Modify Checkout Page object to use Test Data object
- Modify Checkout Steps file to pass Test Data to Checkout Page Objects

## Step 1 : Add another scenario which will be using JSON file as a data source
For simplicity we will be using the same scenario we already have
We will implement differently a step when customer details needs to be entered at the checkout point so it takes data
from our JSON file and we make it **Scenario Outline** which will allow run the same test multiple time every time
using different set of data.
DDTOur Feature file should looks like this:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Scenario: Customer place an order by purchasing an item from search
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart
    And I enter my personal details as follows
      |  first_name  |last_name|     country        |     street_address     |          city     |postcode|phone_number|email_address|
      |TestAutomation| Opencast| United Kingdom (UK)|Hoults Yard, Walker Road|Newcastle upon Tyne|NE6 3PE |07438862327 |test@test.com|
    And I place the order
    Then Order details are successfully verified

  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified
    Examples:
      | customer |
      | Opencast |
      | Testuser |
```
As we can see, we using same steps in our both scenarios apart from  **And I enter <customer> personal details**
We already know that code duplication is not a good practice so it is a great opportunity to use Gherkin **Background**
keyword here to make our Scenario look better:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Background:
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart

  Scenario: Customer place an order by purchasing an item from search
    And I enter my personal details as follows
      |  first_name  |last_name|     country        |     street_address     |          city     |postcode|phone_number|email_address|
      |TestAutomation| Opencast| United Kingdom (UK)|Hoults Yard, Walker Road|Newcastle upon Tyne|NE6 3PE |07438862327 |test@test.com|
    And I place the order
    Then Order details are successfully verified

  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified
    Examples:
      | customer |
      | Opencast |
      | Testuser |
  ```

## Step 2 : Create JSON data set for Customer data
So far we just passed Customer name from feature file, but we need a complete customer details to pass to checkout
page to complete the order. These details we will get from JSON file. We ask JSON file to give us the details of any
particular Customer out of the all Customers Data. As we need multiple customer data we need to create JSON Data in Arrays.

1) Create a New Package under src/test/resources and name it 'testDataResources.
We need to keep all our test resources in the src/test/resources folder, it is better to create a package with in
that to have all the JSON file in it.

2) Create a New File and name it is 'Customer.json'
### Customer.json
```
[
  {
    "firstName": "Opencast",
    "lastName": "Software",
    "age": 30,
    "emailAddress": "opencast@mail.com",
    "address": {
      "streetAddress": "Hoults Yard, Walker Road",
      "city": "Newcastle upon Tyne",
      "postCode": "NE6 3PE",
      "state": "n/a",
      "country": "United Kingdom (UK)",
      "county": "Tyne and Wear"
    },
    "phoneNumber": {
      "home": "012345678",
      "mob": "0987654321"
    }
  },
  {
    "firstName": "Testuser",
    "lastName": "Automation",
    "age": 35,
    "emailAddress": "test@Gmail.com",
    "address": {
      "streetAddress": "1 Summer Gardens",
      "city": "Sunderland",
      "postCode": "SR12 7PE",
      "state": "n/a",
      "country": "United Kingdom (UK)",
      "county": "Tyne and Wear"
    },
    "phoneNumber": {
      "home": "056772211",
      "mob": "0772244180"
    }
  }
]
```
## Step 3 : Write a Java POJO class to represent JSON data
To use this JSON data in the test we need to first deserializes the JSON into an object of the specified class.
And to have the JSON deserialized, a java class object must be created that has the same fields names with the fields
in the JSON string.

1) Create a New Class in scr/main/java under 'testDataType package and name it is 'Customer'
### Customer.java
```
package testDataTypes;

// This is a Java POJO class to represent JSON data
// To use this JSON data in the test we need to first deserializes the JSON into an object of the specified class
// And to have the JSON deserialized, a java class object must be created that has the same fields names with the fields in the JSON string

public class Customer {
    public String firstName;
    public String lastName;
    public int age;
    public String emailAddress;
    public Address address;
    public PhoneNumber phoneNumber;

    public class Address {
        public String streetAddress;
        public String city;
        public String postCode;
        public String state;
        public String country;
        public String county;
    }

    public class PhoneNumber {
        public String home;
        public String mob;
    }
}
```
## Step 4: Prepare ConfigFileReader to read Json path location from Properties
1) First just make an extra entry on the Configuration.properties file to specify the JSON file path
```
testDataResourcePath=src/test/resources/testDataResources/
```
with above complete Configuration file will become like this:

### Configuration.properties
```
environment=local
browser=chrome
windowMaximize=true
driverPath=src/drivers/chromedriver
testDataResourcePath=src/test/resources/testDataResources/
url=http://shop.demoqa.com
implicitWait=10
explicitWait=5
```
2) Create a read method in the Config File Reader class to read JSON file location:
```
public String getTestDataResourcePath(){
        String testDataResourcePath = properties.getProperty("testDataResourcePath");
        if(testDataResourcePath!= null) return testDataResourcePath;
        else throw new RuntimeException("Test Data Resource Path not specified in the Configuration.properties file for the Key: testDataResourcePath");
    }
```
### Explanation:

In the above code, we just get the value saved in the config file for key testDataResourcePath. We throw the exception in case of null value returned from getProperty() method or return the value if it is found not null.

Including above method, the complete Config Reader file will become like this:
```
package dataProviders;

import enums.DriverType;
import enums.EnvironmentType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "configs/Configuration.properties";


    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            //Configuration properties can be easily read from .properties file using object of type Properties provided by java.utils
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getDriverPath(){
        String driverPath = properties.getProperty("driverPath");
        if(driverPath!= null) return driverPath;
        else throw new RuntimeException("driverPath not specified in the Configuration.properties file.");
    }

    public long getCustomWait(String waitTypeKey) {
        String customWait = properties.getProperty(waitTypeKey);
        if(customWait != null) return Long.parseLong(customWait);
        else throw new RuntimeException("Custom Wait not specified in the Configuration.properties file.");
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if(url != null) return url;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

    public DriverType getBrowser() {
        String browserName = properties.getProperty("browser");
        if(browserName == null || browserName.equals("chrome")) return DriverType.CHROME;
        else if(browserName.equalsIgnoreCase("firefox")) return DriverType.FIREFOX;
        else throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
    }

    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");
        if(environmentName == null || environmentName.equalsIgnoreCase("local")) return EnvironmentType.LOCAL;
        else if(environmentName.equals("remote")) return EnvironmentType.REMOTE;
        else throw new RuntimeException("Environment Type Key value in Configuration.properties is not matched : " + environmentName);
    }

    public Boolean getBrowserWindowSize() {
        String windowSize = properties.getProperty("windowMaximize");
        if(windowSize != null) return Boolean.valueOf(windowSize);
        return true;
    }

    public String getTestDataResourcePath(){
        String testDataResourcePath = properties.getProperty("testDataResourcePath");
        if(testDataResourcePath!= null) return testDataResourcePath;
        else throw new RuntimeException("Test Data Resource Path not specified in the Configuration.properties file for the Key: testDataResourcePath");
    }

}
```
## Step 5 : Create a JSON Data Reader class
### How to read JSON  and what is GSON?
**GSON** is an open source code and it’s used a lot in working with JSON and Java.
GSON uses Java Reflection to provide simple methods to convert JSON to java and vice versa.
It can downloaded as GSON jar file from google code website or if used maven, added as a dependency.
1) Add GSON Maven dependency to our pom.xml file:
```
     <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.5</version>
     </dependency>
```
GSON API also supports out of the box JSON to Java Object conversion if the object field names are same as in JSON.
GSON is the main class that exposes the methods fromJson() and toJson() for conversion.
For default implementation, we can create this object directly or we can use GsonBuilder class that provide useful
options for conversion.
2) Create a new class in src/main/java under 'dataProviders' package and name it 'JsonDataReader'
### JsonDataReader.java
```
package dataProviders;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import managers.FileReaderManager;
import testDataTypes.Customer;

public class JsonDataReader {
    private final String customerFilePath = FileReaderManager.getInstance().getConfigReader().getTestDataResourcePath() + "Customer.json";
    private List<Customer> customerList;

    public JsonDataReader(){
        customerList = getCustomerData();
    }
    private List<Customer> getCustomerData() {
        Gson gson = new Gson();
        BufferedReader bufferReader = null;
        try {
            bufferReader = new BufferedReader(new FileReader(customerFilePath));
            Customer[] customers = gson.fromJson(bufferReader, Customer[].class);
            return Arrays.asList(customers);
        }catch(FileNotFoundException e) {
            throw new RuntimeException("Json file not found at path : " + customerFilePath);
        }finally {
            try { if(bufferReader != null) bufferReader.close();}
            catch (IOException ignore) {}
        }
    }
    public final Customer getCustomerByName(String customerName){
        for(Customer customer : customerList) {
            if(customer.firstName.equalsIgnoreCase(customerName)) return customer;
        }
        return null;
    }
}
```
### Explanation:

**getCustomerData() :**
This is a private method, which has the logic implemented to read the Customer Json and save it to the class instance variable. You should be creating more methods like this if you have more test data files like getPaymentOptions(), getProducts() etc.

**JsonDataReader() :**
Here the responsibility of the constructor is to call getCustomerData() method only.

**getCustomerByName() :** This just filter the information and return the specific customer to the test.
## Step 6 : Modify FileReaderManager to return JSsonDataReader object
As we have a FileReaderManager singleton class over all the readers, so we need to make an entry of JsonDataReader
in that as well.

### FileReaderManager.java
```
package managers;

import dataProviders.ConfigFileReader;
import dataProviders.JsonDataReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static JsonDataReader jsonDataReader;

    private FileReaderManager() {
    }
    public static FileReaderManager getInstance( ) {
        return fileReaderManager;
    }
    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }
    public JsonDataReader getJsonReader(){
        return (jsonDataReader == null) ? new JsonDataReader() : jsonDataReader;
    }
}
```
## Step 7: Modify Checkout Page object to use Test Data object
All the setup work is done, it is the time to move closer to the test. First, we need to create a new method inside our
CheckoutPage called **CustomerPersonalDetailsFromJSON**. This method will takes customer data stpred in Customer object
and enter it into our form.

```
public void CustomerPersonalDetailsFromJSON(Customer customer, long customTimeout) throws InterruptedException {

        if(wait.WaitForVisibleWithCustomTimeout(driver,txtbx_FirstName, customTimeout)) {
            txtbx_FirstName.sendKeys(customer.firstName);
            txtbx_LastName.sendKeys(customer.lastName);
            select_Country(customer.address.country);
            txtbx_City.sendKeys(customer.address.city);
            txtbx_Address.sendKeys(customer.address.streetAddress);
            txtbx_PostCode.sendKeys(customer.address.postCode);
            Thread.sleep(2000);
            txtbx_Phone.sendKeys(customer.phoneNumber.mob);
            txtbx_Email.sendKeys(customer.emailAddress);
        }

    }
```
## Step 8 : Modify CheckoutPage Steps file to pass Test Data to Checkout Page Objects
As we already have modified our feature file in the first step, now we need to make necessary changes to the step file
as well.
We need to add a new step which comes from Senario Outline so we run our test first to get a code snippet for it.
1) Run TestRunner and copy a code snippet from the console window to the CheckoutPageSteps definition file.
```
@When("I enter (.+) personal details")
public void i_enter_Opencast_personal_details() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

```
2) Add the following code inside the step:
```
When("I enter (.+) personal details")
    public void enter_personal_details_on_checkout_page(String customerName) throws InterruptedException {

        Customer customer = FileReaderManager.getInstance().getJsonReader().getCustomerByName(customerName);
        checkoutPage.CustomerPersonalDetailsFromJSON(customer, customTimeout);
    }
```
### Explanation :
Fetching the Customer data from json reader using **getCustomerByName()** by passing the Customer Name.
Supplying the same data to the Checkout page objects **CustomerPersonalDetailsFromJSON()** method.
The complete class would look like this:
### CheckoutPageSteps.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import pageObjects.CheckoutPage;
import testDataTypes.Customer;
import testDataTypes.CustomerDataType;
import java.util.List;

public class CheckoutPageSteps {
    TestContext testContext;
    CheckoutPage checkoutPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public CheckoutPageSteps(TestContext context) {
        testContext = context;
        checkoutPage = testContext.getPageObjectManager().getCheckoutPage();
    }

    @When("I enter my personal details as follows")
    public void i_enter_my_personal_details_as_follows(List<CustomerDataType> inputs) {
        checkoutPage.CustomerPersonalDetailsFromDataTable(inputs, customTimeout);

    }

    @When("I enter (.+) personal details")
    public void enter_personal_details_on_checkout_page(String customerName) throws InterruptedException {

        Customer customer = FileReaderManager.getInstance().getJsonReader().getCustomerByName(customerName);
        checkoutPage.CustomerPersonalDetailsFromJSON(customer, customTimeout);
    }

    @When("I place the order")
    public void i_place_the_order() {
        checkoutPage.check_TermsAndCondition(customTimeout);
        checkoutPage.clickOn_PlaceOrder(customTimeout);

    }
}

```

Run TestRunner and test. Now we have 3 tests to execute (one as a scenario and other two as scenario outline with two
different customers). To save a bit of time when testing newly implemented Data Driven Test using JSON code, we can
make a good use of Cucumber tags. Tags are used to filter the test we want to execute at a particular run.
In order to do so, we need to make two small changes to feature file and our test runner class:
1) Add @wip tag above Scenario Outline in our Feature file. (wip stands for Work In Progress and is used while test automation is
in progress)
```
@wip
  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified
    Examples:
      | customer |
      | Opencast |
      | Testuser |
```
2) Add tag property to our Test runner class, so only scenario with @wip tag will be executed:
```
package runners;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"}
)
public class TestRunner {
}
```
Run test with Test Runner Class. Two tests should be executed successfully.

_________________________________________________________________________________________________________________________

# SECTION 13: Cucumber Reports
When ever we do test execution, it is also require to understand the out put of the execution.
## Cucumber Reports
When we executing Cucumber Scenarios, it automatically generates an output in the IDE console.
There is a default behavior associated with that output and we can also configure that output as per our needs.
So how do we modify the default behavior, let’s see this now.
### Pretty Report
The first plugin, we will talk about is **Pretty**.  It provides more verbose output.
To implement this, just specify plugin = “pretty” in **CucumberOptions**.
```
@CucumberOptions( plugin = { “pretty” } )
```
### Monochrome Mode Reporting
If the monochrome option is set to false, then the console output is not as readable as it should be.
The output when the monochrome option is set to false is shown in the above example.
It is just because, if the monochrome is not defined in Cucumber Options, it takes it as false by default.
How to specify it:
```
@CucumberOptions( monochrome = true );
```
CucumberOption code should look like this:
### TestRunner.java
```
package runners;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty" },
        monochrome = true
)
public class TestRunner {
}
```

Console outpul will now look like this:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  @wip
  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file # src/test/resources/features/End2End_Test.feature:17
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified

    Examples:

  Background:                                   # src/test/resources/features/End2End_Test.feature:4
    Given I am on Home Page                     # HomePageSteps.i_am_on_Home_Page()
    When I search for product in dress category # HomePageSteps.i_search_for_product_in_dress_category()
    And I choose to buy the first item          # ProductPageSteps.i_choose_to_buy_the_first_item()
    And I move to checkout from mini cart       # CartPageSteps.i_move_to_checkout_from_mini_cart()

```
### Usage Report
If we are more concerned about the time taken by each Step Definition, then we should use the usage plugin.
This is how we specify the same in @CucumberOptions:
```
@CucumberOptions( plugin = { “usage” })
```
Console output will now look like this:
```
[
  {
    "source": "I choose to buy the first item",
    "steps": [
      {
        "name": "I choose to buy the first item",
        "aggregatedDurations": {
          "average": 5.699256593,
          "median": 5.699256593
        },
        "durations": [
          {
            "duration": 7.602074066,
            "location": "src/test/resources/features/End2End_Test.feature:7"
          },
          {
            "duration": 3.79643912,
            "location": "src/test/resources/features/End2End_Test.feature:7"
          }
        ]
      }
    ]
  },
  {
    "source": "I place the order",
    "steps": [
      {
        "name": "I place the order",
        "aggregatedDurations": {
          "average": 0.392438454,
          "median": 0.392438454
        },
        "durations": [
          {
            "duration": 0.376548105,
            "location": "src/test/resources/features/End2End_Test.feature:19"
          },
          {
            "duration": 0.408328804,
            "location": "src/test/resources/features/End2End_Test.feature:19"
          }
        ]
      }
    ]
  } ...
  ```
## Cucumber Report Output
So far we have seen above is actually good for a test or for couple of tests. But if we run a full test suite,
this report is not much useful in that case. On top of that it is difficult to keep these console output safe for future use.

Cucumber gives us capability to generate reports as well in the form of HTML, XML, JSON & TXT.
Cucumber frameworks generate very good and detailed reports, which can be shared with all stake holders.
There are multiple options available for reports which can be used depending on the requirement.

### Cucumber HTML Reports
For HTML reports, add **html:target/cucumber-reports**  to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty", "html:target/cucumber-reports"  }

)
```
Note: We have specified the path of the Cucumber report, which we want it to generate it under target folder.

Run Test Runner and check HTML report output in target/cucumber-reports folder

### Cucumber JSON Report
For JSON reports, add **json:target/cucumber-reports/Cucumber.json**  to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"}
)
```
Note : This report contains all the information from the gherkin source in the JSON format.
This report is meant to be post processed into another visual format by third-party tools.
JSON report output will look like this:
```
[
  {
    "line": 1,
    "elements": [
      {
        "line": 4,
        "name": "",
        "description": "",
        "type": "background",
        "keyword": "Background",
        "steps": [
          {
            "result": {
              "duration": 4848418704,
              "status": "passed"
            },
            "line": 5,
            "name": "I am on Home Page",
            "match": {
              "location": "HomePageSteps.i_am_on_Home_Page()"
            },
            "keyword": "Given "
          },
          {
            "result": {
              "duration": 3078654424,
              "status": "passed"
            }...
  ```
### Cucumber JUNIT XML Report
For JUNIT reports, add **junit:target/cucumber-reports/Cucumber.xml** to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"
                , "junit:target/cucumber-reports/Cucumber.xml"}
)
```
Note : This report generates XML files just like Apache Ant’s junit report task.
This XML format is understood by most continuous integration servers, who will use it to generate visual reports.
XML Report Output:
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<testsuite failures="0" name="cucumber.runtime.formatter.JUnitFormatter" skipped="0" tests="2" time="57.044076">
<testcase classname="Automated End2End Tests" name="Customer place an order by purchasing an item from search - customer details are taken from JSON file" time="28.534119">
<system-out><![CDATA[Given I am on Home Page.....................................................passed
When I search for product in dress category.................................passed
And I choose to buy the first item..........................................passed
And I move to checkout from mini cart.......................................passed
And I enter Opencast personal details.......................................passed
And I place the order.......................................................passed
Then Order details are successfully verified................................passed
]]></system-out>
</testcase>
<testcase classname="Automated End2End Tests" name="Customer place an order by purchasing an item from search - customer details are taken from JSON file 2" time="28.509957">
<system-out><![CDATA[Given I am on Home Page.....................................................passed
When I search for product in dress category.................................passed
And I choose to buy the first item..........................................passed
And I move to checkout from mini cart.......................................passed
And I enter Testuser personal details.......................................passed
And I place the order.......................................................passed
Then Order details are successfully verified................................passed
]]></system-out>
</testcase>
</testsuite>
```
_________________________________________________________________________________________________________________________

# SECTION 14: Cucumber Extent Report
In the last section of Cucumber Reports  we got to know about the Plugins which are provided by Cucumber itself to generate
various kind of reports like HTML, JSON, XML, etc. Generally, those reports are enough to give us the overall execution results
with detailed time logs and other things. But there are many third party plugins also available which help to produce
reports with improved test logs capacity and better visual graphics.
## Extend Report
One of such plugins is 'Extent Report' by Anshoo Arora. This is currently considered one of the best reporting plugin on the market.
This report fits fine with any test framework you use. With Cucumber as well it works fine but it requires to
have some hacks to produce reports.
It would have been easy if Cucumber also had annotations like @beforeScenario and @beforeFeature.

These annotations are available in SpecFLow which is Cucumber in C#.
But there is an alternative for Cucumber called 'Cucumber Extent Report'.

## Cucumber Extent Report
This plugin is build on Extent Report specifically for Cucumber by Vimal Selvam. This is why it is named
**Cucumber Extent Report**. This one is actually made to ease the implementation of Extent Report in Cucumber Framework.
Let’s start with implementing the same in our Selenium Cucumber Framework.

## Step 1 : Add Cucumber Extent Report dependencies to the Project
The latest version of the extentreports-cucumber4-adapter jar is 1.0.7 which will be primarily used here.

### Cucumber version 4.0.0 to 4.2.0
The extentreports-cucumber4-adapter version 1.0.7 jar from maven repository works by default for the following Cucumber
versions 4.0.0, 4.0.1, 4.0.2, 4.1.0, 4.1.1, 4.2.0.

### Cucumber version 4.2.1 to 4.7.0
The extentreports-cucumber4-adapter version 1.0.7 jar from maven repository does not work by default for
Cucumber versions 4.2.1 - 4.7.0.

The reason is that the adapter requires the class URLOutputStream, which was moved to cucumber.runtime.formatter package
and access changed to package protected in version 4.2.1.
In the previous version it was a public class in cucumber.runtime.io package.
Though this class exists in the github source of extentreports-cucumber4-adapter, it has not been included in the jar.
Therefore, the extentreports-cucumber4-adapter jar needs to be modified for running with Cucumber version greater than 4.2.0

As we are using cucumber version 4.2.0, so extentreports-cucumber4-adapter will work for us by default, however, we need to be mindful
if/when upgrading cucumber to a higher version as it may break our reporting functionality if extentreports-cucumber4-adapter modification
is not addressed as a part of the upgrade process.

1) Add Extent Report Adapter to pom.xml
```
     <dependency>
         <groupId>com.aventstack</groupId>
         <artifactId>extentreports-cucumber4-adapter</artifactId>
         <version>1.0.7</version>
     </dependency>
```
2) Add Extent Report library
```
     <dependency>
         <groupId>com.aventstack</groupId>
         <artifactId>extentreports</artifactId>
         <version>4.0.9</version>
     </dependency>
```

## Step 2: Create Extent Report Configuration file
Extent Config is required by the Cucumber Extent Report plugin to read the report configuration.
It gives us the capability to set many useful setting to the report from the XML configuration file.
By default, it is read from the resources folder in scr/test so
we need to place our Extent Report Configuration file into this specific location.


1) Create a New File in src/test/ inside resources folder and name it 'extent-config.xml'
### extent-config.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<extentreports>
    <configuration>
        <!-- report theme -->
        <!-- standard, dark -->
        <theme>dark</theme>

        <!-- document encoding -->
        <!-- defaults to UTF-8 -->
        <encoding>UTF-8</encoding>

        <!-- protocol for script and stylesheets -->
        <!-- defaults to https -->
        <protocol>https</protocol>

        <!-- title of the document -->
        <documentTitle>Automation Reports</documentTitle>

        <!-- report name - displayed at top-nav -->
        <reportName>Automation Report for Cucumber Framework Tutorial</reportName>

        <!-- report headline - displayed at top-nav, after reportHeadline -->
        <reportHeadline>Behaviour Driven Framework</reportHeadline>

        <!-- global date format override -->
        <!-- defaults to yyyy-MM-dd -->
        <dateFormat>yyyy-MM-dd</dateFormat>

        <!-- global time format override -->
        <!-- defaults to HH:mm:ss -->
        <timeFormat>HH:mm:ss</timeFormat>

        <!-- custom javascript -->
        <scripts>
            <![CDATA[
                $(document).ready(function() {

                });
            ]]>
        </scripts>

        <!-- custom styles -->
        <styles>
            <![CDATA[

            ]]>
        </styles>
    </configuration>
</extentreports>
```
## Step 3: Report Activation
First step of activating the report generation is to place extent.properties file in the src/test/resources folder
to be picked up by the adapter.
1) Create a New File in src/test/ inside resources folder and name it 'extent.properties'
Note: We will only be activating HTML Report in this tutorial:
 - set flag to true for **extent.reporter.html.start** key

 - specify the location for the report file: html report needs to be mentioned as a value for the key

 **extent.reporter.html.config**
 - specify the location for the report path for **extent.reporter.html.out** key
### extent.properties
```
extent.reporter.avent.start=false
extent.reporter.bdd.start=false
extent.reporter.cards.start=false
extent.reporter.email.start=false
extent.reporter.html.start=true
extent.reporter.klov.start=false
extent.reporter.logger.start=false
extent.reporter.tabular.start=false

extent.reporter.avent.config=
extent.reporter.bdd.config=
extent.reporter.cards.config=
extent.reporter.email.config=
extent.reporter.html.config=src/test/resources/ReportsConfig.xml
extent.reporter.klov.config=
extent.reporter.logger.config=
extent.reporter.tabular.config=

extent.reporter.avent.out=
extent.reporter.bdd.out=
extent.reporter.cards.out=
extent.reporter.email.out=
extent.reporter.html.out=test-output/HtmlReport/ExtentHtml.html
extent.reporter.logger.out=
extent.reporter.tabular.out=

```
2) Add **com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:** plugin to @CucumberOptions
inside our Test Runner
### TestRunner.java
```
package runners;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"
                , "junit:target/cucumber-reports/Cucumber.xml"
                , "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class TestRunner {
}
```
The above setup will generate the report in test-output/HtmlReport/ directory with the name of ExtentHtml.html.
Run Test Runner and check that the report has been generated.

# Cucumber Extent Report Features
As it was mentioned above, Cucumber Extent Report provides nice features to make  a report very useful. We will look at them one by one.

## Set System Information in the Report
This gives us a nice feature to set multiple System properties to the report, so that we know under which
system configurations our test suite was executed, when, and by whom.
In order to implement this, we do the following:
1) Create a new Class in src/main/java under utils package and name it 'ExtentReportBuilder'
2) Inside ExtentReportBuilder class we will make use of  WebDriver's getCapabilities()method. I provides information
regarding the current instance of a driver. We will use it for getting browser name and version. We also make use of
System.getProperty() method to get information about Platform and OC version alongside with the details of a user
who excecuted the test.
Extent Report Builder Class should look like this:
```
package utils;

import cucumber.TestContext;
import cucumber.api.Scenario;
import managers.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ExtentReportBuilder {

    TestContext testContext;
    WebDriverManager driverManager;

public ExtentReportBuilder (TestContext context) {
        testContext = context;
        driverManager = testContext.getWebDriverManager();
    }
    public void additionalReportInfo(Scenario scenario)  {
        Capabilities cap = ((RemoteWebDriver) driverManager.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toUpperCase();
        String browserVersion = cap.getVersion();
        scenario.write("Executed by: " + System.getProperty("user.name"));
        scenario.write("Platform: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") v." + System.getProperty("os.version"));
        scenario.write("Browser: " + browserName + " v. " + browserVersion);
    }

}
```
3) Invoke additionalReportInfo() method from @After Hook like so:
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void AfterSteps(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}

```
Run Test Runner and check ExtentHtml.html by opening it in a browser. It should now have System Information in it inside 'Hooks.AfterStep' step;
```
Hooks.AfterSteps(Scenario)
Executed by: testowner
Platform: Mac OS X (x86_64) v.10.14.3
Browser: CHROME v. 75.0.3770.142
```
## Add Screenshot for a Failed Scenario
This feature gives us the capability to embed a screenshot in the report.
Below we will step by step capture a screenshot and attach it to the report, only if the scenario is Failed.
In case of Pass, no screenshot will be taken.
1) Add 'commons-io' library to our pom.xml file

The **Apache Commons-io** library contains utility classes, stream implementations, file filters and other features related to
file manipulations.
```
    <dependency>
         <groupId>commons-io</groupId>
         <artifactId>commons-io</artifactId>
         <version>2.6</version>
    </dependency>
```
 2) Add a new variable to **src/test/resources/extent.properties** file: a path where we save our screenshots:
 ```
 saveScreenshotsTo = test-output/Screenshots/
 ```

 3) Create a new empy forlder inside test-output folder and name it 'Screenshots'


 4) Create a new config reader in src/main/java under 'dataProviders' package and name it
 'ExtentReportConfigReader'. It will read reports related data from extent.properties file

 ### ExtentReportConfigReader.java
 ```
 package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportConfigReader {
    private Properties properties;
    private final String propertyFilePath= "src/test/resources/extent.properties";

    public ExtentReportConfigReader(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try { properties.load(reader); }
            catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }finally {
            try { if(reader != null) reader.close(); }
            catch (IOException ignore) {}
        }
    }

    public String getSaveScreentShotsTo(){
        String saveScreenShotsTo = properties.getProperty("saveScreenshotsTo");
        if(saveScreenShotsTo!= null) return saveScreenShotsTo;
        else throw new RuntimeException("Screenshots path not specified in the Configuration.properties file for the Key: saveScreenshotsTo");
    }

}

```
5) Add ExtentReportConfigReader to FileReader Manager class
### FileReaderManager.java
```
package managers;

import dataProviders.ConfigFileReader;
import dataProviders.ExtentReportConfigReader;
import dataProviders.JsonDataReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static JsonDataReader jsonDataReader;
    private static ExtentReportConfigReader extentReportConfigReader;

    private FileReaderManager() {
    }
    public static FileReaderManager getInstance( ) {
        return fileReaderManager;
    }
    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }
    public JsonDataReader getJsonReader(){
        return (jsonDataReader == null) ? new JsonDataReader() : jsonDataReader;
    }
    public ExtentReportConfigReader getExtentReportConfigReader() {
        return (extentReportConfigReader == null) ? new ExtentReportConfigReader() : extentReportConfigReader;
    }
}

```
6) Add methods to ExtentReportBuilder Class which will capture a screenshot for a failing scenario

### ExtentReportBuilder.java
```
package utils;

import cucumber.TestContext;
import cucumber.api.Scenario;
import managers.FileReaderManager;
import managers.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExtentReportBuilder {

    TestContext testContext;
    WebDriverManager driverManager;
    private  String screenshotName;
    private String saveScreenshotsTo;
    private  String archivedReportName;
    private String saveArchiveReportsTo;
    private String latestReportPath;

    public ExtentReportBuilder (TestContext context) {
        testContext = context;
        driverManager = testContext.getWebDriverManager();
        saveScreenshotsTo = FileReaderManager.getInstance().getExtentReportConfigReader().getSaveScreenShotsTo();
        saveArchiveReportsTo = FileReaderManager.getInstance().getExtentReportConfigReader().getSaveArchiveReportsTo();
        latestReportPath = FileReaderManager.getInstance().getExtentReportConfigReader().getCurrentReportPath();

    }

    public void additionalReportInfo(Scenario scenario)  {
        Capabilities cap = ((RemoteWebDriver) driverManager.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toUpperCase();
        String browserVersion = cap.getVersion();
        scenario.write("Executed by: " + System.getProperty("user.name"));
        scenario.write("Platform: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") v." + System.getProperty("os.version"));
        scenario.write("Browser: " + browserName + " v. " + browserVersion);
    }

    public String dateAdjustment(int number) {
        return (number < 10) ? ("0" + number) : Integer.toString(number);
    }

    public String returnDateStamp() {
        Date d = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        int yr = calendar.get(Calendar.YEAR);
        String year = dateAdjustment(yr);
        int mo = calendar.get(Calendar.MONTH) + 1;
        String month = dateAdjustment(mo);
        int dt = calendar.get(Calendar.DATE);
        String dates = dateAdjustment(dt);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        String hours = dateAdjustment(hr);
        int mn = calendar.get(Calendar.MINUTE);
        String min = dateAdjustment(mn);
        int sc = calendar.get(Calendar.SECOND);
        String sec = dateAdjustment(sc);
        String date = (dates + "_" + month + "_" + year + "_" + hours + "_" + min + "_" + sec);
        return date;
    }

    public String returnScreenshotPath() {
        return (System.getProperty("user.dir") + "/"+ saveScreenshotsTo + screenshotName);
    }

   public void captureScreenshot(Scenario scenario) throws IOException {
           if (scenario.isFailed()) {
               File srcFile = ((TakesScreenshot) driverManager.getDriver()).getScreenshotAs(OutputType.FILE);
               screenshotName = returnDateStamp() + ".png";
               FileUtils.copyFile(srcFile, new File(returnScreenshotPath()));
               scenario.write("<br>");
               scenario.write("Taking a screenshot for a failing step");
               scenario.write("<a target=\"_blank\", href=" + returnScreenshotPath() + ">" + screenshotName);
               scenario.write("<a target=\"_blank\", href=" + returnScreenshotPath() + "><img src=" + returnScreenshotPath() + " height=200 width=300></img></a>");
           }
       }
}

```
### Explanation
**returnDateStamp() :**
is a helper method aiding in constructing screenshot filename which is a date stamp
**returnScreenshotName() :**

Uses returnScreenshotPath() returns file path where screenshot should be saved

**captureScreenshot(Scenario scenario) :**
This method does all the main work - captures screenshot, saves it to a file, constructs file name and assign it to the file and place it into the report

7) Invoke **captureScreenshot()** method from cucumber @after hook

Note: We will give a more meaningful name to our AfterSteps() method. It will be called tearDownAndScreenShotOnFailure()

### Hooks.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void tearDownAndScreenShotOnFailure(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            reportBuilder.captureScreenshot(scenario);
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}
```
Now we all set to test our screenshot taken feature. Let's make a change in your test code so we intentionally failing it.
(e.g. change one of the @FindBy locators to non-existing). Run the TestRunner and check that screenshot is saved at the specified location and is added to the report.


## Archiving previous Reports
When dealing with a real life project, business requirement might be to keep track of all previous test execution results.
In this case we need to find a way how we preserve a previous report before it is overwritten with a new report data.


To achive the above, we will add a new variable into our extent.properties file to specify previous reports storage
location and write some methods which will be responsible for copying a previous report before its being updated after
new test is run. We will make use of our returnDateStamp() method again so we can capture the date,
previous report was archived. We will be following the below steps:

1) Add a new variable to **src/test/resources/extent.properties** file: a path where we save our previous reports:
 ```
 archiveReportsTo = test-output/ArchivedReports/
 ```

 2) Create a new empy forlder inside test-output folder and name it 'ArchivedReports'


 3) Add two methods to our ExtentReportConfigReader Class which will read current report path from "extent.reporter.html.out" key and a path for previous report to be stored;
 ### ExtentReportConfigReader.java
 ```
 package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportConfigReader {
    private Properties properties;
    private final String propertyFilePath= "src/test/resources/extent.properties";

    public ExtentReportConfigReader(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try { properties.load(reader); }
            catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }finally {
            try { if(reader != null) reader.close(); }
            catch (IOException ignore) {}
        }
    }

    public String getSaveScreentShotsTo(){
        String saveScreenShotsTo = properties.getProperty("saveScreenshotsTo");
        if(saveScreenShotsTo!= null) return saveScreenShotsTo;
        else throw new RuntimeException("Screenshots path not specified in the Configuration.properties file for the Key: saveScreenshotsTo");
    }

    public String getSaveArchiveReportsTo(){
        String archiveReportsTo = properties.getProperty("archiveReportsTo");
        if(archiveReportsTo!= null) return archiveReportsTo;
        else throw new RuntimeException("Archived Reports path not specified in the Configuration.properties file for the Key: archiveReportsTo");
    }

    public String getCurrentReportPath(){
        String latestReportPath = properties.getProperty("extent.reporter.html.out");
        if(latestReportPath!= null) return latestReportPath;
        else throw new RuntimeException("Latest Reports path not specified in the Configuration.properties file for the Key: extent.reporter.html.out");
    }

}
```

4) Add two new methods to ExtentReportBuilder Class for renaming and copying previous report file:

```
public void copyFileUsingStream(File source, File dest) throws IOException {

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }catch (Exception e) {
            System.out.println("Unable to copy the report" + e.getMessage());

        } finally {
            is.close();
            os.close();
        }
    }
    public void copyLatestExtentReport() throws IOException {
        archivedReportName = "ArchivedOn_" + returnDateStamp(".html");
        File source = new File(System.getProperty("user.dir") +"/"+ latestReportPath);
        File dest = new File(System.getProperty("user.dir") +"/"+ saveArchiveReportsTo + archivedReportName);
        copyFileUsingStream(source, dest);
    }
```
### Explanation
**copyFileUsingStream() :**
This is a standard Java method for copying files
**copyLatestExtentReport() :**

This method specifies what file needs to be copied and where to. We will invoke this method from our
@after Hook in Hooks Class


5) Invoke **copyLatestExtentReport()** method from cucumber @after hook
### Hooks.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void tearDownAndScreenShotOnFailure(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            reportBuilder.captureScreenshot(scenario);
            reportBuilder.copyLatestExtentReport();
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}
```
 Run the Test Runer and check that the previous report is now stored in ArchivedReport folder. Run test again aand check that another
 previous report is added too.

 _________________________________________________________________________________________________________________________


