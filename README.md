# Hooks in Cucumber Framework
Unlike **TestNG Annotations**, Cucumber supports only two hooks (**@Before** and  **@After**).
They work at the start and the end of the test scenario. As the name suggests, @Before hook gets executed well before
any other test scenario, and @After hook gets executed after executing the scenario.

Implementation of Cucumber Hooks will allow us to to move manipulations with WebDriver (initialisation / closing down)
from Page Objects and Step definitions to Hooks Class.
In order to achieve the above, we will be performing below steps:

- Create a Hook Class and include GetDriver() and ClosingDriver() methods
- Remove closeDriver() method from CheckoutPageSteps definition file

Note: Currently WebDriver is being initialised when we called navigateTo_HomePage() from our @Given Step. This means that technically we may not need to add
**getDriver()** to @Before Hooks. But for the purpose of keeping correct structure of the framework and following separation of concern principle, we make sure that
the first initialisation of a WebDriver happens in the @Before Hook and not during actual test execution


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
### Explanation
@Before Hook is now responsible for Webdriver initialisation and every other Webriver requests by any other
classes during the test execution will receive this instance of a driver

@After Hook is responsible for closing the browser after all test have been executed.

```
## Step 2: Remove closeDriver() method from CheckoutPageSteps definition file
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

