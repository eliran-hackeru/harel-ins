package com.eliranduveen.listeners;

import com.eliranduveen.utils.LogUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        LogUtils.info("🚀 Starting Test Suite: " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        LogUtils.info("🏁 Finished Test Suite: " + suite.getName());
    }
}
