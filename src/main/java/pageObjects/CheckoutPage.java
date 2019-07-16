package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

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


    public void enter_Name(String name) {
        txtbx_FirstName.sendKeys(name);
    }

    public void enter_LastName(String lastName) {
        txtbx_LastName.sendKeys(lastName);
    }

    public void enter_Email(String email) {
        txtbx_Email.sendKeys(email);
    }

    public void enter_Phone(String phone) {
        txtbx_Phone.sendKeys(phone);
    }

    public void enter_City(String city) {
        txtbx_City.sendKeys(city);
    }

    public void enter_Address(String address) {
        txtbx_Address.sendKeys(address);
    }

    public void enter_PostCode(String postCode) {
        txtbx_PostCode.sendKeys(postCode);

    }


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

    public void fill_PersonalDetails() throws InterruptedException {
        enter_Name("TestAutomation");
        enter_LastName("Opencast");
        select_Country("United Kingdom (UK)");
        enter_Address("Hoults Yard, Walker Road");
        enter_City("Newcastle upon Tyne");
        enter_PostCode("NE6 3PE");
        Thread.sleep(2000);
        enter_Phone("07438862327");
        enter_Email("test@test.com");
    }
}