package com.eliranduveen.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.eliranduveen.drivers.DriverFactory.getDriver;

public class LogUtils {

    private static final Logger logger = LogManager.getLogger(LogUtils.class);

    private LogUtils() {
        // Prevent instantiation
    }

    /**
     * Logs an INFO message to both Log4j and Allure report.
     */
    public static void info(String message) {
        logger.info(message);
        AllureStepLogger.logStep(getDriver(), "INFO: " + message);
    }

    /**
     * Logs a WARNING message to both Log4j and Allure report.
     */
    public static void warn(String message) {
        logger.warn(message);
        AllureStepLogger.logStep(getDriver(), "WARNING: " + message);
    }

    /**
     * Logs an ERROR message to both Log4j and Allure report.
     */
    public static void error(String message) {
        logger.error(message);
        AllureStepLogger.logStep(getDriver(), "ERROR: " + message);
    }

    /**
     * Logs a DEBUG message to Log4j (not added to Allure by default).
     */
    public static void debug(String message) {
        logger.debug(message);
    }
}