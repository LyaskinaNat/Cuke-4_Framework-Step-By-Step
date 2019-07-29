# Implicit and Explicit Wait in Selenium WebDriver

In Selenium "Waits" play an important role in executing tests.
## Why Do We Need Waits In Selenium?
Most of the Web applications are developed using Ajax and Javascript.
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
Explicit wait gives better options than an implicit wait as it will wait for dynamically loaded Ajax elements.
Once we declare explicit wait we have to use **"ExpectedConditions"**.

So far in our project we are using **Thread.Sleep()**. This generally is not recommended to use. Only in exeptional conditions, when there is no other options
to work around a particular execution, we can use it.  Thread.Sleep() significantly slows down test execution.
Execution will just stops for specified in Thread.sleep()statement time before continue with
the next statement.
In order to make our test execution more time efficient, we are going to implement a couple of
**Explicit Wait** methods and custom specified timeout time.
In order to implement Explicit Wait we will be performing the below steps:
- Create a New utility class where all Wait methods will be placed
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

    public void navigateTo_HomePage() {
            driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
        }

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
        homePage.navigateTo_HomePage();
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
