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
