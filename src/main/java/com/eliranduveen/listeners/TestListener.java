package com.eliranduveen.listeners;

import com.eliranduveen.utils.LogUtils;
import com.eliranduveen.utils.ScreenshotUtils;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import org.testng.*;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LogUtils.info("🔹 Starting Test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("✅ Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.error("❌ Test Failed: " + result.getMethod().getMethodName());

        Object testClass = result.getInstance();
        try {
            WebDriver driver = (WebDriver) testClass.getClass().getMethod("getDriver").invoke(testClass);
            attachScreenshotToAllure(driver);
            ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
        } catch (Exception e) {
            LogUtils.error("Failed to attach screenshot on failure: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.warn("⚠️ Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        LogUtils.info("==== Test Suite Started: " + context.getName() + " ====");
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtils.info("==== Test Suite Finished: " + context.getName() + " ====");
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshotToAllure(WebDriver driver) {
        return ScreenshotUtils.takeScreenshotAsBytes(driver);
    }
}
