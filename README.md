# Data tables in Cucumber 3 +
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

