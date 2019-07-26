# WebDriver Manager
Why do we need WebDriver Manager?
Till now we have been creating driver within the Step definition file and explicitly tell our script to start Chrome Driver.
Consequences of that are the following:
- Test script handles the logic of creating WebDriver which is not the best coding practice as code inside test steps
should only be responsible for test execution
- Switching between browsers (e.g. Chrome to Firefox) means changing code for every test containing initialisation of a driver

## Design WebDriver Manager
The only responsibility of the WebDriver Manager is to provide the WebDriver, when we ask for it.
To achieve this we will do the following:

- Specify new WebDriver Properties in the Configuration File
- Create Enums for DriverType and EnvironmentType
- Write new Methods to read the above properties
- Design a WebDriver Manager
- Modify the Steps file to use the new WebDriver Manager in the script

## Step 1 : Add WebDriver Properties to the Configuration.properties file
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
4) Create another Enum class 'EnvironmentType' and add Local and Remote environmental variables to it.
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
ConfigFileReader should look like this:
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

**getEnvironment() :** EnvironmentType.Local is returned in case of Null and if the value is equal to Local.
It means that in case of missing environment property, execution will be carried on local machine.

**getBrowser() :** Default value is returned as DriverType.Chrome in case of Null. Exception is thrown if
the value does not match with anything.

## Step 4: Design a WebDriver Manager
Now it is the time to design the WebDriver Manager.
The only thing which we need to keep in mind is that the manager would expose only two method for now which are
**getDriver()** and **closeDriver()**.

**GetDriver()** method will decide if the driver is already created or needs to be created.
**GetDriver()** method further call the method

**createDriver()**, which will decide that
the remote or local driver is needed for execution.

Accordingly, **CreateDriver()** method would make a call let’s say to **createLocalDriver()**.

**CreateLocalDriver()** method will further decide which type of driver needs to be created.

**closeDriver()** method is responsible for closing the browser and will be called after execution of all tests / test steps is completed
1) Create a new file in src/main/java/managers and call it 'WebDriverManager'
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
        webDriverManager.closeDriver();
    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {
        System.out.println("Not implemented");
     }
}
```
Run TestRunner and the test should be executed successfully