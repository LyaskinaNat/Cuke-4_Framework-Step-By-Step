# Hooks in Cucumber Framework
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

