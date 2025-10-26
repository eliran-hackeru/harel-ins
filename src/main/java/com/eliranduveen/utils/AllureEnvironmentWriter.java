package com.eliranduveen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to dynamically create environment.properties
 * for Allure reports based on Configuration.properties values.
 */
public class AllureEnvironmentWriter {

    public static void writeEnvironmentProperties() {
        try {
            // Load values from Configuration.properties
            Properties config = new Properties();
            config.load(AllureEnvironmentWriter.class
                    .getClassLoader()
                    .getResourceAsStream("Configuration.properties"));

            // Create Allure results directory if not exists
            File allureResultsDir = new File("target/allure-results");
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs();
            }

            // Prepare Allure environment properties file
            File environmentFile = new File(allureResultsDir, "environment.properties");
            try (FileOutputStream fos = new FileOutputStream(environmentFile)) {
                Properties envProps = new Properties();

                // Add relevant configuration values
                envProps.setProperty("Base URL", config.getProperty("base.url"));
                envProps.setProperty("Browser", config.getProperty("browser"));
                envProps.setProperty("Headless", config.getProperty("headless"));
                //envProps.setProperty("Implicit Wait (sec)", config.getProperty("implicit.wait"));
                //envProps.setProperty("Explicit Wait (sec)", config.getProperty("explicit.wait"));

                // Add additional system info
                envProps.setProperty("OS", System.getProperty("os.name"));
                envProps.setProperty("Java Version", System.getProperty("java.version"));

                envProps.store(fos, "Allure Environment Properties");
            }

            System.out.println("✅ Allure environment.properties created successfully.");

        } catch (IOException e) {
            System.err.println("❌ Failed to create environment.properties for Allure: " + e.getMessage());
        }
    }
}
