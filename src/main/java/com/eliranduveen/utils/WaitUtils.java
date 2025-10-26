package com.eliranduveen.utils;

import com.eliranduveen.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Simple Wait utilities with overloads for By and WebElement.
 */
public final class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    private static final int EXPLICIT_WAIT_TIMEOUT;
    static {
        int t = 10;
        try {
            String s = null;
            try { s = ConfigManager.getProperty("explicit.wait"); } catch (Exception ignored) {}
            if (s != null && !s.isEmpty()) t = Integer.parseInt(s);
        } catch (Exception ignored) {}
        EXPLICIT_WAIT_TIMEOUT = t;
    }

    private WaitUtils() { /* no instantiation */ }

    // ----------------------
    // Visibility
    // ----------------------
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        logger.debug("Waiting up to {}s for visibility of locator: {}", EXPLICIT_WAIT_TIMEOUT, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        logger.debug("Waiting up to {}s for visibility of element: {}", EXPLICIT_WAIT_TIMEOUT, element);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(element));
    }

    // ----------------------
    // Clickable
    // ----------------------
    public static WebElement waitForClickable(WebDriver driver, By locator) {
        logger.debug("Waiting up to {}s for clickable locator: {}", EXPLICIT_WAIT_TIMEOUT, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        logger.debug("Waiting up to {}s for element to be clickable: {}", EXPLICIT_WAIT_TIMEOUT, element);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // ----------------------
    // Presence / Invisibility / Text / Title
    // ----------------------
    public static WebElement waitForPresence(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForText(WebDriver driver, By locator, String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForTitleContains(WebDriver driver, String titlePart) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.titleContains(titlePart));
    }

    public static void shortSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
