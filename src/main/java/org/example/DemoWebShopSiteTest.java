package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
public class DemoWebShopSiteTest {
    private final String shopUrl = "https://demowebshop.tricentis.com";
    private WebDriver driver;
    private Utils utils;

    private FirefoxDriver getFirefoxDriver() {
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile firefoxProfile = profile.getProfile("default");
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.setProfile(firefoxProfile);
        return new FirefoxDriver(options);
    }

    private ChromeDriver getChromeDriver() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    @BeforeClass
    public void beforeClass() {
        this.driver = getChromeDriver();
        // this.driver = this.getFirefoxDriver();

        this.utils = new Utils(driver);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
    @Test()
    public void registerUser() {
        this.driver.get(this.shopUrl);

        String testEmail = Utils.generateRandomEmail();

        this.utils.clickVisibilityElement("//a[contains(@href,'register')]");

        this.utils.clickVisibilityElement("//*[@id='gender-male']");

        WebElement firstName = this.driver.findElement(By.id("FirstName"));
        firstName.sendKeys("Olena");

        WebElement lastName = this.driver.findElement(By.id("LastName"));
        lastName.sendKeys("Zhymkova");

        WebElement email = this.driver.findElement(By.id("Email"));
        email.sendKeys(testEmail);

        WebElement password = this.driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1");

        WebElement confirmPassword = this.driver.findElement(By.id("ConfirmPassword"));
        confirmPassword.sendKeys("Qwerty1"+ Keys.ENTER);

        String expected = "Log out";
        String actual = this.utils.getVisibilityElementText("//a[contains(@href,'logout')]");

        Assert.assertEquals(actual, expected);
    }

    @Test()
    public void login() {
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'login')]");

        WebElement email = this.driver.findElement(By.id("Email"));
        email.sendKeys("olenkatyut@gmail.com");

        WebElement password = this.driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1" + Keys.ENTER);

        String expected = "Log out";
        String actual = this.utils.getVisibilityElementText("//a[contains(@href,'logout')]");

        Assert.assertEquals(actual, expected);

        this.utils.clickVisibilityElement("//a[contains(@href,'logout')]");
    }

    @Test()
    public void checkSubgroups() {
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'computers')]");

        String[] expectedItems = {
                "desktops",
                "notebooks",
                "accessories"
        };

        for (String expectedItem : expectedItems) {
            String xPathElement = String.format("//a[@href='/computers']//a[contains(@href,'%s')]", expectedItem);
            Assert.assertNotNull(xPathElement);
        }
    }

    @Test()
    public void checkSorting() {
        this.driver.get(this.shopUrl + "/desktops");

        WebElement selectElementBefore = this.driver.findElement(By.name("products-orderby"));
        Select selectBefore = new Select(selectElementBefore);
        selectBefore.selectByVisibleText("Price: Low to High");

        List<WebElement> priceElements = this.driver.findElements(By.xpath("//span[contains(@class, 'actual-price')]"));
        List<Double> prices = new ArrayList<>();

        for (WebElement element : priceElements) {
            prices.add(Double.valueOf(element.getText()));
        }

        List<Double> sortedPrices = prices.stream().sorted().toList();

        Assert.assertNotEquals(sortedPrices.size(), 0);
        Assert.assertEquals(sortedPrices, prices);

        WebElement selectElementAfter = this.driver.findElement(By.name("products-orderby"));
        Select selectAfter = new Select(selectElementAfter);
        selectAfter.selectByVisibleText("Name: A to Z");

        List<WebElement> titleElements = this.driver.findElements(By.xpath("//h2[@class='product-title']//a"));
        List<String> titles = new ArrayList<>();

        for (WebElement element : titleElements) {
            titles.add(element.getText());
        }

        List<String> sortedTitles = titles.stream().sorted().toList();

        Assert.assertNotEquals(sortedTitles.size(), 0);
        Assert.assertEquals(sortedTitles, titles);
    }

    @Test()
    public void checkItemCount() {
        this.driver.get(this.shopUrl + "/desktops");

        WebElement selectElementBefore = this.driver.findElement(By.name("products-pagesize"));
        Select selectBefore = new Select(selectElementBefore);
        selectBefore.selectByVisibleText("8");

        List<WebElement> productElementsBefore = this.driver.findElements(By.xpath("//div[@class='product-item']"));
        Assert.assertEquals(productElementsBefore.size(), 6);

        WebElement selectElementAfter = this.driver.findElement(By.name("products-pagesize"));
        Select selectAfter = new Select(selectElementAfter);
        selectAfter.selectByVisibleText("4");

        List<WebElement> productElementAfter = this.driver.findElements(By.xpath("//div[@class='product-item']"));
        Assert.assertEquals(productElementAfter.size(), 4);
    }

    @Test()
    public void addToWishlist(){
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'apparel-shoes')]");

        this.utils.clickVisibilityElement("//*[@class='product-title']//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')]");

        String emptyWishlist = this.utils.getVisibilityElementText("//a[contains(@href,'wishlist')]//span[@class = 'wishlist-qty']");

        this.utils.clickVisibilityElement("//*[@id='add-to-wishlist-button-5']");

        String actualWishlist = this.utils.getVisibilityElementText("//a[contains(@href,'wishlist')]//span[@class = 'wishlist-qty']");

        Assert.assertNotEquals(emptyWishlist, actualWishlist);
    }

    @Test()
    public void addToCart() {
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'apparel-shoes')]");

        this.utils.clickVisibilityElement("(//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')])[1]");

        String emptyCart = this.utils.getVisibilityElementText("//span[@class='cart-qty']");
        Assert.assertEquals(emptyCart, "(0)");

        this.utils.clickVisibilityElement("//*[@id='add-to-cart-button-5']");

        String cartWithProduct = this.utils.getVisibilityElementText("//span[@class='cart-qty' and text()='(1)']");
        Assert.assertEquals(cartWithProduct, "(1)");
    }

    @Test()
    public void removeFromCart() {
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'apparel-shoes')]");

        this.utils.clickVisibilityElement("(//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')])[1]");

        this.utils.clickVisibilityElement("//*[@id='add-to-cart-button-5']");

        this.utils.clickVisibilityElement("//*[@id='topcartlink']//a[contains(@href,'cart')]");

        this.utils.clickVisibilityElement("//input[contains(@name,'removefromcart')]");

        this.utils.clickVisibilityElement("//input[contains(@name,'updatecart')]");

        String actualCart = this.utils.getVisibilityElementText("//div[contains(@class,'order-summary-content')]");

        String expectedMessage = "Your Shopping Cart is empty!";

        Assert.assertEquals(actualCart, expectedMessage);
    }

    @Test()
    public void checkout() {
        this.driver.get(this.shopUrl);

        this.utils.clickVisibilityElement("//a[contains(@href,'login')]");

        WebElement email = this.driver.findElement(By.id("Email"));
        email.sendKeys("olenkatyut@gmail.com");

        WebElement password = this.driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1");

        this.utils.clickVisibilityElement("//input[@type='submit' and contains(@class, 'login-button')]");

        this.utils.clickVisibilityElement("//a[contains(@href,'apparel-shoes')]");

        this.utils.clickVisibilityElement("//*[@class='product-item']//a[contains(@href,'/50s-rockabilly-polka-dot-top-jr-plus-size')]");

        this.utils.clickVisibilityElement("//*[@id='add-to-cart-button-5']");

        this.utils.clickVisibilityElement("//*[@id='topcartlink']//a[contains(@href,'cart')]");

        this.utils.clickVisibilityElement("//*[@id='termsofservice']");

        this.utils.clickVisibilityElement("//*[@id='checkout']");

        this.utils.clickVisibilityElement("//*[@id='billing-buttons-container']/input");

        this.utils.clickVisibilityElement("//*[@id='shipping-buttons-container']/input");

        this.utils.clickVisibilityElement("//*[@id='shipping-method-buttons-container']/input");

        this.utils.clickVisibilityElement("//*[@id='payment-method-buttons-container']/input");

        this.utils.clickVisibilityElement("//*[@id='payment-info-buttons-container']/input");

        this.utils.clickVisibilityElement("//*[@id='confirm-order-buttons-container']/input");

        String actual = this.utils.getVisibilityElementText("//div[contains(@class,'order-completed')]//strong");

        String expectedMessage = "Your order has been successfully processed!";

        Assert.assertEquals(actual, expectedMessage);

        this.utils.clickVisibilityElement("//a[contains(@href,'logout')]");
    }
}
