# Cucumber end-to-end test
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
(Note: Cucmber-java and cucumber-junit dependencies required to be of the same version. 
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
        //And click on chekout button
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
 
Out end-to-end test should be executed sucessfully

