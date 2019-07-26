# Sharing Test Context between steps and Step Definition files
## Why do we need to share Test Context?
Dividing Cucumber Steps between many classes may be a good idea. It is, however, probably not needed early in a project.
When we write our first scenario, we will most likely only have just a few steps. The first class with steps is probably
small and we can easily find our way around it.

But a scenario in Cucumber is a series of steps which gets executed one after another. Each step may have some state
which can be required by other step in the scenario. In other words, some steps may depend on
previous steps. This means that we must be able to share state/collected data between steps.

Also when a number of tests grows as project matures, keeping all the steps in a single Step Definition class quickly becomes
difficult to manage. To overcome this,  different classes for different features / scenarios / steps are used instead.

Now we have a new problem – objects we create in one step class may be needed in the other step classes as well.

Back to our case, till now we just had one scenario which had few steps and we kept all the steps in the same
Step definition file. In a real life project there are tens or even hundreds of scenarios and step definition files.
And often there is a need to share the Test Context (including Scenario Context, Test State,
data collected during test step execution) with all the Step Definition classes.
Cucumber supports several Dependency Injection (DI) Containers – it simply tells a DI container to instantiate
step definition classes and wire them up correctly. One of the supported DI containers is **PicoContainer**.

## What is PicoContainer?
**PicoContainer** is a small library which doesn’t require set up of any configuration and
use of any APIs such as @Inject. It uses constructors instead.

**PicoContainer** really only has a one functionality – it instantiates objects.
Simply hand it some classes and it will instantiate each one, correctly wiring together via their constructors.
Cucumber scans your classes with step definitions in them, passes them to PicoContainer,
then asks it to create new instances for every scenario.

We will be performing below steps to implement data sharing across steps:

- Add PicoContainer dependency to the Project
- Create a Test Context class which will hold all the objects state
- Divide the Steps class into multiple steps classes with logical separation
- Write Constructor to share Test Context

## Step 1: Add PicoContainer Dependency to the Maven Project
```
       <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-picocontainer</artifactId>
            <version>${cucumber-picocontainer.version}</version>
            <scope>test</scope>
        </dependency>
```

Note: It is suggested to use the same cucumber-picocontainer version as cucumber version. In our case it is 4.2.0
We also need to create a new property **cucumber-picocontainer.version** inside <properties> tag of pom.xml where we specify
picocontainer version:
```
    <properties>
        <cucumber.version>4.2.0</cucumber.version>
        <cucumber-picocontainer.version>4.2.0</cucumber-picocontainer.version>
    </properties>

```
## Step 2 : Create a Test Context class
We should create this class logically: identify all information our Step definition file is using
and put that information into this class. In our case our Steps.java is using the following information:

- PageObjects : Provided by PageObjectManager
- WebDriver : Provided by WebDriverManager
- Properties : Provided by FileReaderManager

So, we need the above objects in our Test Context class.
Next, if we look at the objects, we see that our FileReaderManager is already a Singleton Class and to use it we don’t need
to create an instance of it. It creates it by itself. So no need to add FileReaderManager to TestContext class,
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
We kept the initialisation in the constructor and created getMethods() for both objects.
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
### HomePageSteps.java (after copying step definitions related to HomePage to its own HomePage step definition class and before implementing
TextContext)
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

Now with just adding Constructor to HomePageSteps class and passing **TestContext** as a Parameter to constructor
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