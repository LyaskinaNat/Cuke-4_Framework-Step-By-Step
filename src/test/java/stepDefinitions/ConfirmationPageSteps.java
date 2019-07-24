package stepDefinitions;

import managers.FileReaderManager;
import org.junit.Assert;
import cucumber.TestContext;
import cucumber.api.java.en.Then;
import enums.Context;
import pageObjects.ConfirmationPage;


public class ConfirmationPageSteps {
    TestContext testContext;
    ConfirmationPage confirmationPage;
    public long customTimeout;

    public ConfirmationPageSteps(TestContext context) {
        testContext = context;
        confirmationPage = testContext.getPageObjectManager().getConfirmationPage();
        customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    }

    @Then("Order details are successfully verified")
    public void order_details_are_successfully_verified() {

        String productName = ((String) testContext.scenarioContext.getContext(Context.PRODUCT_NAME)).toLowerCase();
        Assert.assertTrue(confirmationPage.getProductNames(customTimeout).stream().filter(x -> x.contains(productName)).findFirst().get().length() > 0);

    }
}
