package com.eliranduveen.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

import static com.eliranduveen.drivers.DriverFactory.getDriver;

public class AllureStepLogger {

    /**
     * Logs a step to Allure with an optional screenshot.
     */
    public static void logStep(WebDriver driver, String message) {
        Allure.step(message);
        attachScreenshot(driver, message);
    }

    private static void attachScreenshot(WebDriver driver, String stepName) {
        try {
            byte[] screenshot = ScreenshotUtils.takeScreenshotAsBytes(driver);
            Allure.addAttachment("Screenshot - " + stepName, new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            System.err.println("⚠️ Failed to attach screenshot: " + e.getMessage());
        }
    }
}