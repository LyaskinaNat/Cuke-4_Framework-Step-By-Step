package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import managers.FileReaderManager;
import cucumber.TestContext;
import pageObjects.HomePage;


public class HomePageSteps {

    HomePage homePage;
    TestContext testContext;
    long customTimeout = FileReaderManager.getInstance().getConfigReader().getCustomWait("explicitWait");

    public HomePageSteps(TestContext context) {
        testContext = context;
        homePage = testContext.getPageObjectManager().getHomePage();
    }

    @Given("I am on Home Page")
    public void i_am_on_Home_Page() {
        homePage.navigateTo_HomePage();

    }

    @When("I search for product in dress category")
    public void i_search_for_product_in_dress_category() {
        homePage.perform_Search("dress", customTimeout);

    }
}
