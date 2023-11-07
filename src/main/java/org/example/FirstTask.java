package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;


import java.time.Duration;

public class FirstTask {
    @Test()
    public void checkTitle() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/");

        String pageTitle = driver.getTitle();

        String expectedTitle = "EPAM | Software Engineering & Product Development Services";
        Assert.assertEquals(pageTitle, expectedTitle);

        driver.quit();
    }
    @Test()
    public void switchTheme() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/");

        WebElement themeSwitcher = driver.findElement(By.xpath("(//*[@id='wrapper']//section[@class='theme-switcher-ui'] //div[@class='theme-switcher'])[2]"));
        themeSwitcher.click();

        String headerColor = driver.findElement(By.xpath("//*[@id='wrapper']//div[@class='header__inner']")).getCssValue("background-color");

        String expectedTheme = "rgba(0, 0, 0, 0)";
        Assert.assertEquals(headerColor, expectedTheme);

        driver.quit();
    }

    @Test()
    public void switchLang() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/");

        WebElement langComponentDefault = driver.findElement(By.xpath("//*[@id='wrapper']//button[@class='location-selector__button']"));
        langComponentDefault.click();

        WebElement UaLanguage = driver.findElement(By.xpath("(//*[@id='wrapper']//a[@href='https://careers.epam.ua'])[2]"));
        UaLanguage.click();

        WebElement langComponentUA = driver.findElement(By.xpath("//*[@id='wrapper']//button[@class='location-selector__button']"));
        String actual = langComponentUA.getText();

        String expectedLang = "Україна (UA)";
        Assert.assertEquals(actual, expectedLang);

        driver.quit();
    }

    @Test()
    public void checkPoliciesList() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/");

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

        driver.quit();
    }

    @Test()
    public void switchLocationByRegion() {
        WebDriver driver = new ChromeDriver();
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        driver.get("https://www.epam.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']")));
        WebElement cookieBanner = driver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']"));
        cookieBanner.click();

        String[] expectedItems = {
            "AMERICAS",
            "EMEA",
            "APAC"
        };

        for (String expectedItem : expectedItems) {
            String xPathElement = String.format("//a[contains(@class,'js-tabs-link') and contains(text(),'%s')]", expectedItem);

            WebElement locationTab = driver.findElement(By.xpath(xPathElement));
            locationTab.click();
        }

       driver.quit();
    }

    @Test()
    public void searchFunc() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/");

        WebElement searchIcon = driver.findElement(By.xpath("//*[@id='wrapper']//span[@class='search-icon dark-iconheader-search__search-icon']"));
        searchIcon.click();

        WebElement searchField = driver.findElement(By.xpath("//*[@id='new_form_search']"));
        searchField.click();
        searchField.sendKeys("AI" + Keys.ENTER);

        WebElement searchResult = driver.findElement(By.xpath("//*[@id='main']//span[@class='rte-text-gradient gradient-text']"));
        String actual = searchResult.getText();

        String expectedLang = "Search";
        Assert.assertEquals(actual, expectedLang);

        driver.quit();
    }

    @Test()
    public void checkValidation() {
        WebDriver driver = new ChromeDriver();

        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        driver.get("https://www.epam.com/about/who-we-are/contact");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']")));
        WebElement cookieBanner = driver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']"));
        cookieBanner.click();

        WebElement submitButton = driver.findElement(By.xpath("//button[contains(@class,'button-ui')]"));
        submitButton.click();

        String firstNameFieldAtr = driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_first_name")).getAttribute("aria-invalid");
        String lastNameFieldAtr = driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_last_name")).getAttribute("aria-invalid");
        String emailFieldAtr = driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_email")).getAttribute("aria-invalid");
        String phoneFieldAtr = driver.findElement(By.id("_content_epam_en_about_who-we-are_contact_jcr_content_content-container_section_section-par_form_constructor_user_phone")).getAttribute("aria-invalid");
        String textFieldAtr = driver.findElement(By.xpath("//span[contains(@aria-describedby,'user_comment_how_hear_about-error')]")).getAttribute("aria-invalid");
        String consentFieldAtr = driver.findElement(By.xpath("//*[contains(@id,'new_form_gdprConsent_')]")).getAttribute("aria-invalid");

        Assert.assertEquals(firstNameFieldAtr, "true");
        Assert.assertEquals(lastNameFieldAtr, "true");
        Assert.assertEquals(emailFieldAtr, "true");
        Assert.assertEquals(phoneFieldAtr, "true");
        Assert.assertEquals(textFieldAtr, "true");
        Assert.assertEquals(consentFieldAtr, "true");

        driver.quit();
    }

    @Test()
    public void logoRedirect() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.epam.com/about");

        WebElement logo = driver.findElement(By.xpath("(//*[@id='wrapper']//img[@class='header__logo header__logo-light'])[1]"));
        logo.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "https://www.epam.com/");

        driver.quit();
    }

    @Test()
    public void downloadReport() throws InterruptedException {
     WebDriver driver = new ChromeDriver();
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        driver.get("https://www.epam.com/about");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']")));
        WebElement cookieBanner = driver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']"));
        cookieBanner.click();

        WebElement downloadButton = driver.findElement(By.xpath("//a[contains(@href,'EPAM_Corporate_Overview')]"));
        String[] hrefParts = downloadButton.getAttribute("href").split("/");
        String fileName = hrefParts[hrefParts.length - 1];

        downloadButton.click();

        String downloadsFolderPath = "/Users/Olena_Tiutiunnyk/Downloads";

        File file = new File(downloadsFolderPath, fileName);
        Thread.sleep(2000);

        Assert.assertTrue(file.exists());

        file.deleteOnExit();
        driver.quit();
    }

}