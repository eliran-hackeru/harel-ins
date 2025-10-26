package com.eliranduveen.tests;

import com.eliranduveen.base.BaseTest;
import com.eliranduveen.config.ConfigManager;
import com.eliranduveen.pages.TravelPolicyPage;
import com.eliranduveen.utils.AllureStepLogger;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Travel-Policy")
@Feature("First-Time Purchase")
@Story("User completes first-time travel insurance purchase flow")
@Severity(SeverityLevel.CRITICAL)
public class FirstTimePurchaseTest extends BaseTest {

    @Test(
            description = "Verify user can complete first-time purchase flow successfully",
            retryAnalyzer = com.eliranduveen.listeners.RetryAnalyzer.class
    )
    public void testPurchaseFlow() {

        // Initialize the Travel Policy page object
        TravelPolicyPage travelPolicyPage = new TravelPolicyPage(getDriver());

        // Step 1: Open the base URL
        getDriver().get(ConfigManager.getBaseUrl());
        AllureStepLogger.logStep(getDriver(), "Opened base URL: " + ConfigManager.getBaseUrl());

        // Step 2: Click "First-time Purchase" button
        travelPolicyPage.goToFirstTime();
        AllureStepLogger.logStep(getDriver(), "Clicked on 'First-time Purchase' button");

        // Step 3: Select a random continent
        travelPolicyPage.selectRandomContinent("אנטארקטיקה");
        AllureStepLogger.logStep(getDriver(), "Selected a random continent");

        // Step 4: Click "Next to travel dates"
        travelPolicyPage.clickNextToTravelDates();
        AllureStepLogger.logStep(getDriver(), "Clicked 'Next to travel dates'");

        // Step 5: Select travel dates (7 days from now, returning after 30 days)
        travelPolicyPage.selectTravelDates();
        AllureStepLogger.logStep(getDriver(), "Selected departure and return dates");

        // Step 6: Verify total days are displayed correctly
        Assert.assertTrue(travelPolicyPage.verifyTotalDays(), "Total days not displayed correctly");
        AllureStepLogger.logStep(getDriver(), "Verified total days calculation");

        // Step 7: Click "Next to passenger details"
        travelPolicyPage.clickNextToPassengerDetails();
        AllureStepLogger.logStep(getDriver(), "Clicked 'Next to passenger details' button");

        // Step 8: Verify passenger details page is displayed
        Assert.assertTrue(travelPolicyPage.isPassengerDetailsPageDisplayed(), "Passenger details page did not open");
        AllureStepLogger.logStep(getDriver(), "Verified passenger details page opened successfully");
    }
}