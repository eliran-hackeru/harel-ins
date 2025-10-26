package com.eliranduveen.drivers;

import com.eliranduveen.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    // Thread-safe WebDriver storage for parallel tests
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // Prevent instantiation
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(initDriver());
        }
        return driver.get();
    }

    private static WebDriver initDriver() {
        String browser = ConfigManager.getBrowser().toLowerCase();

        switch (browser) {
            case "chrome":
                return createChromeDriver();
            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Run headless if configured (especially for CI/cloud environments)
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless=new"); // modern headless mode
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Set Chrome binary location if provided by environment (for Heroku, etc.)
        String chromeBinary = System.getenv("GOOGLE_CHROME_BIN");
        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        }

        // Set ChromeDriver path if provided by environment
        String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
        if (chromeDriverPath != null && !chromeDriverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }

        return new ChromeDriver(options);
    }

    public static void quitDriver() {
        WebDriver drv = driver.get();
        if (drv != null) {
            drv.quit();
            driver.remove();
        }
    }
}