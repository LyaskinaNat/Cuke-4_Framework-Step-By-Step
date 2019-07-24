package pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Waits;


public class ConfirmationPage {
    WebDriver driver;
    Waits wait;

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);
    }

    @FindAll(@FindBy(css = ".order_item"))
    public List<WebElement> prd_List;

    @FindBy(css = ".woocommerce-order-details")
    public WebElement orderDetails;


    public List<String> getProductNames(long customTimeout) {
        List<String> productNames = new ArrayList<>();
        if (wait.WaitForVisibleWithCustomTimeout(driver, orderDetails, customTimeout)) {
            for (WebElement element : prd_List) {
                productNames.add(element.findElement(By.cssSelector(".product-name")).getText());
            }
        }
        return productNames;
    }


}

