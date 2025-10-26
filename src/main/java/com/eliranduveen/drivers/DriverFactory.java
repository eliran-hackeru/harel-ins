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
        return driver.get();
    }

    public static void initDriver() {
        if (driver.get() == null) {
            String browser = ConfigManager.getBrowser().toLowerCase();
            if (browser.equals("chrome")) {
                WebDriverManager.chromedriver().setup(); // ✅ auto-download matching version
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                if (ConfigManager.isHeadless()) options.addArguments("--headless=new");
                driver.set(new ChromeDriver(options));
            }
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