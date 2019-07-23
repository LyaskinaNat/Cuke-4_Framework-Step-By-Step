package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Waits;

public class CartPage {

    WebDriver driver;
    Waits wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        wait = new Waits();
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".cart-button")
    public WebElement btn_Cart;

    @FindBy(css = ".checkout-button.alt")
    public WebElement btn_ContinueToCheckout;


    public void clickOn_Cart(long customTimeout) {
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_Cart, customTimeout)) {
            btn_Cart.click();
        }
    }

    public void clickOn_ContinueToCheckout(long customTimeout){
        if(wait.WaitForVisibleWithCustomTimeout(driver,btn_ContinueToCheckout, customTimeout)) {
            btn_ContinueToCheckout.click();
        }
    }

}
