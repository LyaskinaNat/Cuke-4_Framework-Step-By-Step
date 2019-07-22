package pageObjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePage {

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);

    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}
