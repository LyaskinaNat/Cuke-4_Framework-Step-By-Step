package pageObjects;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class ProductListingPage {

    public ProductListingPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "button.single_add_to_cart_button")
    public WebElement btn_AddToCart;

    @FindAll(@FindBy(css = ".noo-product-inner"))
    public List<WebElement> prd_List;

    @FindBy(id="pa_color")
    public WebElement selectColour;

    @FindBy(id="pa_size")
    public WebElement selectSize;


    public void select_Product(int productNumber) {
        prd_List.get(productNumber).click();
    }

    public void makeSelection(int index) {
        Select colour = new Select(selectColour);
        colour.selectByIndex(index);
        Select size  = new Select(selectSize);
        size.selectByIndex(index);
    }

    public void clickOn_AddToCart() {
        btn_AddToCart.click();
    }

}
