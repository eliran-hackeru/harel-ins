package com.eliranduveen.listeners;

import com.eliranduveen.utils.LogUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = 2; // retry once by default

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LogUtils.warn("🔁 Retrying test: " + result.getMethod().getMethodName() +
                    " | Attempt: " + (retryCount + 1));
            return true;
        }
        return false;
    }
}