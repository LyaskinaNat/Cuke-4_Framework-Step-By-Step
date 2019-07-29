package pageObjects;

import managers.FileReaderManager;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Waits;


public class HomePage {
    WebDriver driver;
    Waits wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);

    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void navigateTo_HomePage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
    }

    public void perform_Search(String search, long customTimeout) {
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_Search, customTimeout)) {
            btn_Search.click();
            input_Search.sendKeys(search);
            input_Search.sendKeys(Keys.RETURN);
        }

    }
}
