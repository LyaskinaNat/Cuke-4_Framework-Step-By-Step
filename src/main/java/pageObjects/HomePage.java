package pageObjects;

import dataProviders.ConfigFileReader;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import managers.FileReaderManager;

public class HomePage {

    WebDriver driver;
    ConfigFileReader configFileReader;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        configFileReader= new ConfigFileReader();
    }

    @FindBy(css=".noo-search")
    public WebElement btn_Search;

    @FindBy(css=".form-control")
    public WebElement input_Search;

    public void navigateTo_HomePage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
    }

    public void perform_Search(String search) {
        btn_Search.click();
        input_Search.sendKeys(search);
        input_Search.sendKeys(Keys.RETURN);
    }
}
