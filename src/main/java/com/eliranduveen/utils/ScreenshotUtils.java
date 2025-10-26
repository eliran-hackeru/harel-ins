package com.eliranduveen.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides screenshot utilities:
 *  - captureScreenshot(driver, testName) -> saves a file and attaches to Allure
 *  - takeScreenshotAsBytes(driver) -> returns bytes for direct Allure attachment
 *  - captureOnFailure(driver, testName) -> convenience wrapper
 */

public final class ScreenshotUtils {

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot and attaches it to Allure report.
     */
    public static void captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) return;
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // Attach to Allure (in-memory)
            Allure.addAttachment("Screenshot - " + testName, new ByteArrayInputStream(bytes));

            // Save to disk (optional)
            String screenshotsPath = com.eliranduveen.config.ConfigManager.getScreenshotsPath();
            Path dir = Paths.get(screenshotsPath);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            String fileName = testName + "_" + System.currentTimeMillis() + ".png";
            Path out = dir.resolve(fileName);
            Files.write(out, bytes);
            com.eliranduveen.utils.LogUtils.info("Saved screenshot: " + out.toAbsolutePath());
        } catch (IOException e) {
            com.eliranduveen.utils.LogUtils.error("Failed to save screenshot: " + e.getMessage());
        } catch (Exception e) {
            com.eliranduveen.utils.LogUtils.error("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Returns screenshot as byte[] (useful for @Attachment methods).
     */
    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        if (driver == null) return new byte[0];
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            com.eliranduveen.utils.LogUtils.error("Failed to take screenshot bytes: " + e.getMessage());
            System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Captures a screenshot on failure (used in listeners).
     */
    public static void captureOnFailure(WebDriver driver, String testName) {
        captureScreenshot(driver, "Failure - " + testName);
    }

    /**
     * Capture a screenshot and attach it to Allure report.
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
        }
    }
}