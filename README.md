# Read Project Configurations from Property File
So far in our project we have been storing hard coded values inside the project code.
It is against the coding principles to do so as it makes our test less manageable and maintainable.
Therefore with the help of 'properties' file we will be focusing on eliminating these hard coded values.

### What is a Properties file in Java
**.properties** files are mainly used in Java programs to maintain project **configuration data, database config,
project settings, etc**.
Each parameter in .properties file is stored as a pair of strings, in key-value pair format.
You can easily read properties from this file using **object of type Properties**. This is a utility provided by Java itself:
```
java.util.Properties;
```
### Advantages of .properties file in Java
If any information is changed from the properties file, you don’t need to recompile the java class.
In other words, the advantage of using properties file is we can configure things which are prone to change
over a period of time without a need of changing test code.
## Step 1: Create a .properties file
1) Create a New Folder and name it 'configs', by right click on the root Project and select New >> Folder.
2) Create a New File by right click on the above created folder and name it 'Configuration.properties'
3) Write Hard Coded Values in the Configuration.properties File.

So far there are three hard coded values we will move to our **Configuration.properties**:
### Configuration.properties
```
driverPath=src/drivers/chromedriver
url=http://shop.demoqa.com
implicitWait=5
```
## Step 2: Create a Config File Reader
1) Create a New Package under src/main/java/ and name it 'dataProviders'.
We will keep all the data reader files here in this package.

2) Create a New Class file inside 'dataProviders' package and name it 'ConfigFileReader'.
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
}

```
## Explanation
### How to Load Properties File
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
the Value of the matching key from the .properties file.
If the properties file does not have the specified key, it returns null.
This is why we have put the null check and in case of null we like to throw an exception with the stack trace information
and stop the test.

## Step 3: Use ConfigFileReader object in the Steps.java file and HomePage.java file
To use the **ConfigFileReader object** in the test, we need to fist create an object of the class.
```
ConfigFileReader configFileReader= new ConfigFileReader();
```


Then we can replace the below statement
```
System.setProperty(“webdriver.chrome.driver”,“src/drivers/chromedriver”);
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


Note: Generally, it is bad practice to create object of property class in every class which requires it.
We have created the object of the Properties Class in Steps file and another object of Properties Class again
in the HomePage class.

We will cover how to overcome this issue in the next section.

Run TestRunner and the test should be executed successfully