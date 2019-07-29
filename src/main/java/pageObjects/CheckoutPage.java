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

                if(wait.WaitForVisibleWithCustomTimeout(driver,txtbx_FirstName, customTimeout)) {

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