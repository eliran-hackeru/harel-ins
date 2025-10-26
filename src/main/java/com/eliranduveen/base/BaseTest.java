package com.eliranduveen.base;


import com.eliranduveen.config.ConfigManager;
import com.eliranduveen.drivers.DriverFactory;
import com.eliranduveen.utils.AllureEnvironmentWriter;
import com.eliranduveen.utils.ScreenshotUtils;
import com.eliranduveen.utils.LogUtils;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    // Thread-local WebDriver reference
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeSuite
    public void setUpEnvironment() {
        AllureEnvironmentWriter.writeEnvironmentProperties();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        DriverFactory.initDriver();
        LogUtils.info("========== Starting test: " + method.getName() + " ==========");
        driver.set(DriverFactory.getDriver());

        WebDriver drv = getDriver();
        drv.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
        drv.manage().window().maximize();
        drv.get(ConfigManager.getBaseUrl());

        LogUtils.info("Browser launched: " + ConfigManager.getBrowser());
        LogUtils.info("Navigated to: " + ConfigManager.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                LogUtils.error("❌ Test FAILED: " + testName);
                attachScreenshotToAllure(getDriver());
                ScreenshotUtils.captureScreenshot(getDriver(), testName);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                LogUtils.info("✅ Test PASSED: " + testName);
            } else if (result.getStatus() == ITestResult.SKIP) {
                LogUtils.warn("⚠️ Test SKIPPED: " + testName);
            }
        } finally {
            DriverFactory.quitDriver();
            LogUtils.info("========== Finished test: " + testName + " ==========\n");
            driver.remove();
        }
    }

    protected WebDriver getDriver() {
        return driver.get();
    }

    // Allure screenshot attachment (for reporting)
    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] attachScreenshotToAllure(WebDriver driver) {
        return ScreenshotUtils.takeScreenshotAsBytes(driver);
    }
}
