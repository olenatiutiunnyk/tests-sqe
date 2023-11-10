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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;


import java.time.Duration;
import java.util.Objects;

public class EpamSiteTest {
    private final String epamUrl = "https://www.epam.com/";
    private WebDriver driver;

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
        //this.driver = this.getFirefoxDriver();

        this.driver.manage().window().maximize();

        this.driver.get(epamUrl);

        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']")));
        WebElement cookieBanner = this.driver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']"));
        cookieBanner.click();
    }

    @BeforeTest
    void addDelay() throws InterruptedException {
        Thread.sleep(1500);
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

        String pageTitle = driver.getTitle();

        String expectedTitle = "EPAM | Software Engineering & Product Development Services";
        Assert.assertEquals(pageTitle, expectedTitle);
    }

    @Test()
    public void switchTheme() {
        this.driver.get(this.epamUrl);

        WebElement themeSwitcher = driver.findElement(By.xpath("(//*[@id='wrapper']//section[@class='theme-switcher-ui'] //div[@class='theme-switcher'])[2]"));
        themeSwitcher.click();

        String headerColor = driver.findElement(By.xpath("//*[@id='wrapper']//div[@class='header__inner']")).getCssValue("background-color");

        String expectedTheme = "rgba(0, 0, 0, 0)";
        Assert.assertEquals(headerColor, expectedTheme);
    }

    @Test()
    public void switchLang() {
        this.driver.get(this.epamUrl);

        WebElement langComponentDefault = driver.findElement(By.xpath("//*[@id='wrapper']//button[@class='location-selector__button']"));
        langComponentDefault.click();

        WebElement UaLanguage = driver.findElement(By.xpath("(//*[@id='wrapper']//a[@href='https://careers.epam.ua'])[2]"));
        UaLanguage.click();

        WebElement langComponentUA = driver.findElement(By.xpath("//*[@id='wrapper']//button[@class='location-selector__button']"));
        String actual = langComponentUA.getText();

        String expectedLang = "Україна (UA)";
        Assert.assertEquals(actual, expectedLang);
    }

    @Test()
    public void checkPoliciesList() {
        this.driver.get(this.epamUrl);

        WebElement policiesList = driver.findElement(By.xpath("//*[@class='policies-links-wrapper']"));
        String policiesText = policiesList.getText();

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

            WebElement locationTab = this.driver.findElement(By.xpath(xPathElement));
            locationTab.click();
        }
    }

    @Test()
    public void searchFunc() throws InterruptedException {
        this.driver.get(this.epamUrl);

        WebElement searchIcon = this.driver.findElement(By.xpath("//*[@id='wrapper']//span[@class='search-icon dark-iconheader-search__search-icon']"));
        searchIcon.click();

        WebElement searchField = this.driver.findElement(By.xpath("//*[@id='new_form_search']"));
        searchField.click();
        searchField.sendKeys("AI" + Keys.ENTER);
        Thread.sleep(1000);

        WebElement searchResult = this.driver.findElement(By.xpath("//*[@id='main']//span[@class='rte-text-gradient gradient-text']"));
        String actual = searchResult.getText();

        String expectedLang = "Search";
        Assert.assertEquals(actual, expectedLang);
    }

    @Test()
    public void checkValidation() {
        this.driver.get(this.epamUrl + "about/who-we-are/contact");

        WebElement submitButton = this.driver.findElement(By.xpath("//button[contains(@class,'button-ui')]"));
        submitButton.click();

        String firstNameFieldAtr = this.driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_first_name")).getAttribute("aria-invalid");
        String lastNameFieldAtr = this.driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_last_name")).getAttribute("aria-invalid");
        String emailFieldAtr = this.driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_email")).getAttribute("aria-invalid");
        String phoneFieldAtr = this.driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_phone")).getAttribute("aria-invalid");
        String textFieldAtr = this.driver.findElement(By.xpath("//span[contains(@aria-describedby,'user_comment_how_hear_about-error')]")).getAttribute("aria-invalid");
        String consentFieldAtr = this.driver.findElement(By.xpath("//*[contains(@id,'new_form_gdprConsent_')]")).getAttribute("aria-invalid");

        Assert.assertEquals(firstNameFieldAtr, "true");
        Assert.assertEquals(lastNameFieldAtr, "true");
        Assert.assertEquals(emailFieldAtr, "true");
        Assert.assertEquals(phoneFieldAtr, "true");
        Assert.assertEquals(textFieldAtr, "true");
        Assert.assertEquals(consentFieldAtr, "true");
    }

    @Test()
    public void logoRedirect() {
        this.driver.get(this.epamUrl + "about");

        WebElement logo = this.driver.findElement(By.xpath("(//*[@id='wrapper']//img[@class='header__logo header__logo-light'])[1]"));
        logo.click();

        String currentUrl = this.driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, this.epamUrl);
    }

    @Test()
    public void downloadReport() throws InterruptedException {
        String aboutPage = this.epamUrl + "about";

        this.driver.get(aboutPage);

        WebElement downloadButton = this.driver.findElement(By.xpath("//a[contains(@href,'EPAM_Corporate_Overview')]"));
        String[] hrefParts = downloadButton.getAttribute("href").split("/");
        String fileName = hrefParts[hrefParts.length - 1];

        downloadButton.click();

        if (!Objects.equals(this.driver.getCurrentUrl(), aboutPage)) {
            this.driver.close();
        }

        String downloadsFolderPath = "/Users/Olena_Tiutiunnyk/Downloads";

        File file = new File(downloadsFolderPath, fileName);
        Thread.sleep(2000);

        Assert.assertTrue(file.exists());

        file.deleteOnExit();
    }
}