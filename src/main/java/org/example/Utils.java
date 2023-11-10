package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

public class Utils {

    WebDriverWait wait;
    WebDriver driver;
    public Utils(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.driver = driver;
    }

    public static String generateRandomEmail () {
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        return "olenkatyut+test_" + uniqueId + "@gmail.com";
    }

    public void clickVisibilityElement (String xpath) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        WebElement element = this.driver.findElement(By.xpath(xpath));
        element.click();
    }

    public String getVisibilityElementText (String xpath) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        WebElement element = this.driver.findElement(By.xpath(xpath));
        return element.getText();
    }

    public String getVisibilityElementAttribute (String xpath, String attributeName) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        WebElement element = this.driver.findElement(By.xpath(xpath));
        return element.getAttribute(attributeName);
    }
}
