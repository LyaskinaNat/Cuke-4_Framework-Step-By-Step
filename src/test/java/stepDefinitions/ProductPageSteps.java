package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.java.en.When;
import enums.Context;
import managers.FileReaderManager;
import pageObjects.ProductListingPage;

public class ProductPageSteps {

    TestContext testContext;
    ProductListingPage productListingPage;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");


    public ProductPageSteps(TestContext context) {
        testContext = context;
        productListingPage = testContext.getPageObjectManager().getProductListingPage();
    }

    @When("I choose to buy the first item")
    public void i_choose_to_buy_the_first_item() {
        productListingPage.select_Product(0, customTimeout);
        String productName = productListingPage.getProductName(customTimeout);
        testContext.scenarioContext.setContext(Context.PRODUCT_NAME, productName);
        productListingPage.makeSelection(1, customTimeout );
        productListingPage.clickOn_AddToCart(customTimeout);
    }
}
