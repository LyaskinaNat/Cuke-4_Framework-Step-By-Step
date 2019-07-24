# Data Driven Testing using Json with Cucumber
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



