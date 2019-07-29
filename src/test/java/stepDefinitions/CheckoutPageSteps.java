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
    public void i_enter_personal_details(String customerName) throws InterruptedException {

        Customer customer = FileReaderManager.getInstance().getJsonReader().getCustomerByName(customerName);
        checkoutPage.CustomerPersonalDetailsFromJSON(customer, customTimeout);
    }

    @When("I place the order")
    public void i_place_the_order() {
        checkoutPage.check_TermsAndCondition(customTimeout);
        checkoutPage.clickOn_PlaceOrder(customTimeout);

    }
}
