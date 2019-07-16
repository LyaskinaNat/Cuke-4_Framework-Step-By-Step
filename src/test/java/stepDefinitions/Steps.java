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
