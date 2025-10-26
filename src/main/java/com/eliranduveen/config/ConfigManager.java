package com.eliranduveen.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE_PATH =
            "src/main/resources/Configuration.properties";

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Configuration.properties file.", e);
        }
    }

    private ConfigManager() {
        // Prevent instantiation
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Property '" + key + "' is missing or empty in Configuration.properties");
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static String getReportsPath() {
        return getProperty("reports.path");
    }

    public static String getScreenshotsPath() {
        return getProperty("screenshots.path");
    }

    public static String getLogLevel() {
        return getProperty("log.level");
    }

    public static String getEnvironment() {
        return getProperty("env");
    }
}
