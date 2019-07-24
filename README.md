# Share data between steps in Cucumber using Scenario Context
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