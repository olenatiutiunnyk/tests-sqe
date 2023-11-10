package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;

public class EpamSiteTest {
    private final String epamUrl = "https://www.epam.com";
    private WebDriver driver;
    private Utils utils;

    private FirefoxDriver getFirefoxDriver() {
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile firefoxProfile = profile.getProfile("default"); // "default" - назва профілю
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
        firefoxProfile.setPreference("pdfjs.disabled", true);
        FirefoxOptions options = new FirefoxOptions();
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
        this.driver = this.getChromeDriver();
        // this.driver = this.getFirefoxDriver();

        this.driver.get(epamUrl);

        this.utils = new Utils(driver);

        this.utils.clickVisibilityElement("//*[@id='onetrust-accept-btn-handler']");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test()
    public void checkTitle() {
        this.driver.get(this.epamUrl);

        String pageTitle = this.driver.getTitle();

        String expectedTitle = "EPAM | Software Engineering & Product Development Services";
        Assert.assertEquals(pageTitle, expectedTitle);
    }

    @Test()
    public void switchTheme() {
        this.driver.get(this.epamUrl);

        this.utils.clickVisibilityElement("(//*[@id='wrapper']//section[@class='theme-switcher-ui']//div[@class='theme-switcher'])[2]");

        WebElement wrapper = this.driver.findElement(By.xpath("//*[@id='wrapper']//div[@class='header__inner']"));
        String headerColor = wrapper.getCssValue("background-color");

        String expectedTheme = "rgba(0, 0, 0, 0)";
        Assert.assertEquals(headerColor, expectedTheme);
    }

    @Test()
    public void switchLang() {
        this.driver.get(this.epamUrl);

        this.utils.clickVisibilityElement("//*[@id='wrapper']//button[@class='location-selector__button']");

        this.utils.clickVisibilityElement("(//*[@id='wrapper']//a[@href='https://careers.epam.ua'])[2]");

        String expectedLang = "Україна (UA)";
        String actual = this.utils.getVisibilityElementText("//*[@id='wrapper']//button[@class='location-selector__button']");

        Assert.assertEquals(actual, expectedLang);
    }

    @Test()
    public void checkPoliciesList() {
        this.driver.get(this.epamUrl);

        String policiesText = this.utils.getVisibilityElementText("//*[@class='policies-links-wrapper']");

        String[] expectedItems = {
            "INVESTORS",
            "COOKIE POLICY",
            "OPEN SOURCE",
            "APPLICANT PRIVACY NOTICE",
            "PRIVACY POLICY",
            "WEB ACCESSIBILITY"
        };

        for (String expectedItem : expectedItems) {
            Assert.assertTrue(policiesText.contains(expectedItem));
        }
    }

    @Test()
    public void switchLocationByRegion() {
        this.driver.get(this.epamUrl);

        String[] expectedItems = {
            "AMERICAS",
            "EMEA",
            "APAC"
        };

        for (String expectedItem : expectedItems) {
            String xPathElement = String.format("//a[contains(@class,'js-tabs-link') and contains(text(),'%s')]", expectedItem);
            this.utils.clickVisibilityElement(xPathElement);
        }
    }

    @Test()
    public void searchFunc() {
        this.driver.get(this.epamUrl);

        this.utils.clickVisibilityElement("//*[@id='wrapper']//span[@class='search-icon dark-iconheader-search__search-icon']");

        WebElement searchField = this.driver.findElement(By.xpath("//*[@id='new_form_search']"));
        searchField.sendKeys("AI" + Keys.ENTER);

        String expectedLang = "Search";
        String actual = this.utils.getVisibilityElementText("//*[@id='main']//span[@class='rte-text-gradient gradient-text']");

        Assert.assertEquals(actual, expectedLang);
    }

    @Test()
    public void checkValidation() {
        this.driver.get(this.epamUrl + "/about/who-we-are/contact");

        this.utils.clickVisibilityElement("//button[contains(@class,'button-ui')]");

        String firstNameValid = this.utils.getVisibilityElementAttribute("//input[@name='user_first_name']", "aria-invalid");
        String lastNameFieldValid = this.utils.getVisibilityElementAttribute("//input[@name='user_last_name']", "aria-invalid");
        String emailFieldValid = this.utils.getVisibilityElementAttribute("//input[@name='user_email']", "aria-invalid");
        String phoneFieldValid = this.utils.getVisibilityElementAttribute("//input[@name='user_phone']", "aria-invalid");
        String textFieldValid = this.utils.getVisibilityElementAttribute("//span[contains(@aria-describedby,'user_comment_how_hear_about-error')]", "aria-invalid");
        String consentFieldValid = this.utils.getVisibilityElementAttribute("//*[contains(@id,'new_form_gdprConsent_')]", "aria-invalid");

        Assert.assertEquals(firstNameValid, "true");
        Assert.assertEquals(lastNameFieldValid, "true");
        Assert.assertEquals(emailFieldValid, "true");
        Assert.assertEquals(phoneFieldValid, "true");
        Assert.assertEquals(textFieldValid, "true");
        Assert.assertEquals(consentFieldValid, "true");
    }

    @Test()
    public void logoRedirect() {
        this.driver.get(this.epamUrl + "/about");

        this.utils.clickVisibilityElement("(//*[@id='wrapper']//img[@class='header__logo header__logo-light'])[1]");

        String currentUrl = this.driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, this.epamUrl + "/");
    }

    @Test()
    public void downloadReport() throws InterruptedException {
        String aboutPage = this.epamUrl + "/about";

        this.driver.get(aboutPage);

        WebElement downloadButton = this.driver.findElement(By.xpath("//a[contains(@href,'EPAM_Corporate_Overview')]"));
        String[] hrefParts = downloadButton.getAttribute("href").split("/");
        String fileName = hrefParts[hrefParts.length - 1];

        downloadButton.click();

        String downloadsFolderPath = "/Users/Olena_Tiutiunnyk/Downloads";

        File file = new File(downloadsFolderPath, fileName);
        Thread.sleep(2000);

        Assert.assertTrue(file.exists());

        file.deleteOnExit();
    }
}