package com.eliranduveen.listeners;

import com.eliranduveen.drivers.DriverFactory;
import com.eliranduveen.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotUtils.attachScreenshot(DriverFactory.getDriver(),
                "Failure Screenshot - " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ScreenshotUtils.attachScreenshot(DriverFactory.getDriver(),
                "Skipped Test Screenshot - " + result.getName());
    }
}