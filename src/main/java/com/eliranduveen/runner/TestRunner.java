package com.eliranduveen.runner;

import com.eliranduveen.config.ConfigManager;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.TestNG;
import org.testng.annotations.Listeners;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.XmlClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TestRunner dynamically builds and executes TestNG suites.
 * This allows flexibility to run tests programmatically (e.g., CI/CD, Jenkins, Heroku).
 */
//@Listeners({AllureTestNg.class})
public class TestRunner {

    public static void main(String[] args) {

        // Create TestNG instance
        TestNG testng = new TestNG();

        // Create a suite
        XmlSuite suite = new XmlSuite();
        suite.setName("UI Automation Suite");
        suite.setParallel(XmlSuite.ParallelMode.TESTS);
        suite.setThreadCount(2);

        // Create a test block
        String testName = ConfigManager.getProperty("testName");
        if (testName == null || testName.isEmpty()) {
            testName = "Default Test";
        }
        XmlTest test = new XmlTest(suite);
        test.setName(testName);

        // Add classes to test
        List<XmlClass> testClasses = new ArrayList<>();
        testClasses.add(new XmlClass("com.eliranduveen.tests.FirstTimePurchaseTest"));
        test.setXmlClasses(testClasses);

        // Add suite to TestNG
        testng.setTestSuites(Collections.singletonList("src/main/resources/testng.xml"));

        // Run tests
        testng.run();
    }
}
