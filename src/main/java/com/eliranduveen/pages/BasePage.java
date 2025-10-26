package com.eliranduveen.pages;

import com.eliranduveen.utils.AllureStepLogger;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import static com.eliranduveen.drivers.DriverFactory.getDriver;

public abstract class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Clicks an element only if it exists and is visible.
     * Can be used from page objects or directly from tests.
     */
    public static void clickIfVisible(WebDriver driver, By locator) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                AllureStepLogger.logStep(getDriver(), "Clicked visible element: " + locator);
            }
        } catch (NoSuchElementException ignored) {
            AllureStepLogger.logStep(getDriver(), "Element not visible: " + locator);
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }
}
