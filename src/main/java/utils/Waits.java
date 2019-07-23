package utils;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waits {

    public boolean WaitForClickableWithCustomTimeout(WebDriver driver, WebElement element, long customTimeout) {

        try {

            final WebDriverWait customWait;
            customWait= new WebDriverWait(driver, 10);
            customWait.until(ExpectedConditions.elementToBeClickable(element));
            System.out.println("Element is clickable, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            return true;

        } catch (Exception e) {
            System.out.println("Unable to click on WebElement, locator: " + "<" + element.getClass() + ">" + ", custom Timeout: " + customTimeout);
            Assert.fail("Unable to click on WebElement, Exception: " + e.getMessage());
            return false;
        }
    }
    public boolean WaitForVisibleWithCustomTimeout(WebDriver driver, WebElement element, long customTimeout) {

        try {

            final WebDriverWait customWait;
            customWait= new WebDriverWait(driver, customTimeout);
            customWait.until(ExpectedConditions.visibilityOf(element));
            System.out.println("Successfully found WebElement, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            return true;

        } catch (Exception e) {
            System.out.println("Unable to find WebElement, locator: " + "<" + element + ">" + ", custom Timeout: " + customTimeout);
            Assert.fail("Unable to find WebElement, Exception: " + e.getMessage());
            return false;
        }
    }

 }

