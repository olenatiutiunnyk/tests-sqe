package org.example;

import com.beust.ah.A;
import com.google.common.collect.Ordering;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestSecondTask {

    private final String shopUrl = "https://demowebshop.tricentis.com/";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void beforeClass() {
        this.driver = new ChromeDriver();

        //this.driver = new FirefoxDriver();

        driver.manage().window().maximize();

        this.driver.get(shopUrl);

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeTest
    void addDelay() throws InterruptedException {
        Thread.sleep(5000);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
    @Test()
    public void registerUser() throws InterruptedException {
        this.driver.get(this.shopUrl);

        Thread.sleep(1500);

        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        String testEmail = "olenkatyut+test_" + uniqueId + "@gmail.com";

        WebElement registerLink = driver.findElement(By.xpath("//a[contains(@href,'register')]"));
        registerLink.click();

        WebElement genderMale = driver.findElement(By.id("gender-male"));
        genderMale.click();

        WebElement firstName = driver.findElement(By.id("FirstName"));
        firstName.sendKeys("Olena");

        WebElement lastName = driver.findElement(By.id("LastName"));
        lastName.sendKeys("Zhymkova");

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys(testEmail);

        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1");

        WebElement confirmPassword = driver.findElement(By.id("ConfirmPassword"));
        confirmPassword.sendKeys("Qwerty1"+ Keys.ENTER);

        String expected = "Log out";
        WebElement logoutLink = driver.findElement(By.xpath("//a[contains(@href,'logout')]"));
        String actual = logoutLink.getText();

        Assert.assertEquals(actual, expected);
    }

    @Test()
    public void login() {
        this.driver.get(this.shopUrl);

        WebElement loginLink = driver.findElement(By.xpath("//a[contains(@href,'login')]"));
        loginLink.click();

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys("olenkatyut@gmail.com");

        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1" + Keys.ENTER);

        String expected = "Log out";
        WebElement logoutLink = driver.findElement(By.xpath("//a[contains(@href,'logout')]"));
        String actual = logoutLink.getAttribute("text");

        Assert.assertEquals(actual, expected);
    }

    @Test()
    public void checkSubgroups() {
        this.driver.get(this.shopUrl);

        WebElement computerCategory = driver.findElement(By.xpath("//a[contains(@href,'computers')]"));
        computerCategory.click();

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
        this.driver.get(this.shopUrl);

        WebElement selectElement1 = driver.findElement(By.name("products-orderby"));
        Select select1 = new Select(selectElement1);
        select1.selectByVisibleText("Price: Low to High");

        List<WebElement> priceElements = driver.findElements(By.xpath("//span[contains(@class, 'actual-price')]"));
        List<Double> prices = new ArrayList<>();

        for (WebElement element : priceElements) {
            prices.add(Double.valueOf(element.getText()));
        }

        List<Double> sortedPrices = prices.stream().sorted().toList();

        Assert.assertNotEquals(sortedPrices.size(), 0);
        Assert.assertEquals(sortedPrices, prices);

        WebElement selectElement2 = driver.findElement(By.name("products-orderby"));
        Select select2 = new Select(selectElement2);
        select2.selectByVisibleText("Name: A to Z");

        List<WebElement> titleElements = driver.findElements(By.xpath("//h2[@class='product-title']//a"));
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
        this.driver.get(this.shopUrl);

        WebElement selectElement1 = driver.findElement(By.name("products-pagesize"));
        Select select1 = new Select(selectElement1);
        select1.selectByVisibleText("8");

        List<WebElement> productElements = driver.findElements(By.xpath("//div[@class='product-item']"));
        Assert.assertEquals(productElements.size(), 6);

        WebElement selectElement2 = driver.findElement(By.name("products-pagesize"));
        Select select2 = new Select(selectElement2);
        select2.selectByVisibleText("4");

        List<WebElement> productElements2 = driver.findElements(By.xpath("//div[@class='product-item']"));
        Assert.assertEquals(productElements2.size(), 4);
    }

    @Test()
    public void addToWishlist() throws InterruptedException {
        this.driver.get(this.shopUrl);

        WebElement shoesCategory = driver.findElement(By.xpath("//a[contains(@href,'apparel-shoes')]"));
        shoesCategory.click();

        WebElement shoesProduct = driver.findElement(By.xpath("(//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')])[1]"));
        shoesProduct.click();

        WebElement wishlistBeforeAdding = driver.findElement(By.xpath("//a[contains(@href,'wishlist')]//span[@class = 'wishlist-qty']"));
        String emptyWishlist = wishlistBeforeAdding.getText();

        WebElement wishlistIcon = driver.findElement(By.xpath("//*[@id='add-to-wishlist-button-5']"));
        wishlistIcon.click();

        Thread.sleep(2000);

        WebElement addToWishlistSuccess = driver.findElement(By.xpath("//a[contains(@href,'wishlist')]//span[@class = 'wishlist-qty']"));
        String actualWishlist = addToWishlistSuccess.getText();

        Assert.assertNotEquals(emptyWishlist, actualWishlist);
    }

    @Test()
    public void addToCart() throws InterruptedException {
        this.driver.get(this.shopUrl);

        WebElement shoesCategory = driver.findElement(By.xpath("//a[contains(@href,'apparel-shoes')]"));
        shoesCategory.click();

        WebElement shoesProduct = driver.findElement(By.xpath("(//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')])[1]"));
        shoesProduct.click();

        WebElement cartBeforeAdding = driver.findElement(By.xpath("//a[contains(@href,'cart')]//span[@class = 'cart-qty']"));
        String emptyCart = cartBeforeAdding.getText();

        WebElement cartButton = driver.findElement(By.xpath("//*[@id='add-to-cart-button-5']"));
        cartButton.click();

        Thread.sleep(2000);

        WebElement addToCartSuccess = driver.findElement(By.xpath("//a[contains(@href,'cart')]//span[@class = 'cart-qty']"));
        String actualCart = addToCartSuccess.getText();

        Assert.assertNotEquals(emptyCart, actualCart);
    }

    @Test()
    public void removeFromCart() throws InterruptedException {
        this.driver.get(this.shopUrl);

        WebElement shoesCategory = driver.findElement(By.xpath("//a[contains(@href,'apparel-shoes')]"));
        shoesCategory.click();

        WebElement shoesProduct = driver.findElement(By.xpath("(//a[contains(@href,'50s-rockabilly-polka-dot-top-jr-plus-size')])[1]"));
        shoesProduct.click();

        WebElement cartButton = driver.findElement(By.xpath("//*[@id='add-to-cart-button-5']"));
        cartButton.click();

        Thread.sleep(2000);

        WebElement cartMenu = driver.findElement(By.xpath("//*[@id='topcartlink']//a[contains(@href,'cart')]"));
        cartMenu.click();

        WebElement removeFromCartCheckbox = driver.findElement(By.xpath("//input[contains(@name,'removefromcart')]"));
        removeFromCartCheckbox.click();

        WebElement updateCart = driver.findElement(By.xpath("//input[contains(@name,'updatecart')]"));
        updateCart.click();

        WebElement emptyCartMessage = driver.findElement(By.xpath("//div[contains(@class,'order-summary-content')]"));
        String actualCart = emptyCartMessage.getText();

        String expectedMessage = "Your Shopping Cart is empty!";

        Assert.assertEquals(actualCart, expectedMessage);
    }

    @Test()
    public void checkout() {
        this.driver.get(this.shopUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginLink = driver.findElement(By.xpath("//a[contains(@href,'login')]"));
        loginLink.click();

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys("olenkatyut@gmail.com");

        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys("Qwerty1" + Keys.ENTER);

        WebElement shoesCategory = driver.findElement(By.xpath("//a[contains(@href,'apparel-shoes')]"));
        shoesCategory.click();

        WebElement shoesProduct = driver.findElement(By.xpath("//*[@class='product-item']//a[contains(@href,'/50s-rockabilly-polka-dot-top-jr-plus-size')]"));
        shoesProduct.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='add-to-cart-button-5']")));
        WebElement cartButton = driver.findElement(By.xpath("//*[@id='add-to-cart-button-5']"));
        cartButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='topcartlink']//a[contains(@href,'cart')]")));
        WebElement cartMenu = driver.findElement(By.xpath("//*[@id='topcartlink']//a[contains(@href,'cart')]"));
        cartMenu.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='termsofservice']")));
        WebElement acceptTerms = driver.findElement(By.xpath("//*[@id='termsofservice']"));
        acceptTerms.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='checkout']")));
        WebElement checkoutButton = driver.findElement(By.xpath("//*[@id='checkout']"));
        checkoutButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='billing-buttons-container']/input")));
        WebElement passFirstStep = driver.findElement(By.xpath("//*[@id='billing-buttons-container']/input"));
        passFirstStep.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='shipping-buttons-container']/input")));
        WebElement passSecondStep = driver.findElement(By.xpath("//*[@id='shipping-buttons-container']/input"));
        passSecondStep.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='shipping-method-buttons-container']/input")));
        WebElement passThirdStep = driver.findElement(By.xpath("//*[@id='shipping-method-buttons-container']/input"));
        passThirdStep.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='payment-method-buttons-container']/input")));
        WebElement passFourthStep = driver.findElement(By.xpath("//*[@id='payment-method-buttons-container']/input"));
        passFourthStep.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='payment-info-buttons-container']/input")));
        WebElement passFifthStep = driver.findElement(By.xpath("//*[@id='payment-info-buttons-container']/input"));
        passFifthStep.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='confirm-order-buttons-container']/input")));
        WebElement confirmOrder = driver.findElement(By.xpath("//*[@id='confirm-order-buttons-container']/input"));
        confirmOrder.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'order-completed')]//strong")));
        WebElement successCheckout = driver.findElement(By.xpath("//div[contains(@class,'order-completed')]//strong"));
        String actual = successCheckout.getText();

        String expectedMessage = "Your order has been successfully processed!";

        Assert.assertEquals(actual, expectedMessage);
    }

}
