package com.eliranduveen.pages;

import com.eliranduveen.utils.AllureStepLogger;
import com.eliranduveen.utils.WaitUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.eliranduveen.drivers.DriverFactory.getDriver;
import static com.eliranduveen.utils.WaitUtils.*;

public class TravelPolicyPage extends BasePage {

    public TravelPolicyPage(WebDriver driver) {
        super(driver);
    }

    // ========== ELEMENTS ==========

    @FindBy(className = "jss11")
    WebElement firstTimePurchaseButton;

    @FindBy(css = ".MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-6.MuiGrid-grid-md-3")
    List<WebElement> continentOptions;

    @FindBy(xpath = "//button[.//span[contains(text(),'הלאה לבחירת תאריכי הנסיעה')]]")
    WebElement nextToTravelDatesButton;

    @FindBy(css = "input#travel_start_date[data-hrl-bo='startDateInput_input']")
    WebElement departureDateInput;

    @FindBy(css = "input#travel_end_date[data-hrl-bo='endDateInput_input']")
    WebElement returnDateInput;

    @FindBy(css = "span[data-hrl-bo='total-days']")
    WebElement totalDaysLabel;

    @FindBy(xpath = "//span[@class='MuiButton-label' and normalize-space(text())='הלאה לפרטי הנוסעים']/parent::button")
    WebElement nextToPassengerDetailsButton;

    @FindBy(xpath = "//span[@data-hrl-bo='traveler_info_0' and normalize-space(text())='פרטי הנוסע הראשון']/ancestor::div[contains(@class,'jss305')]")
    WebElement passengerDetailsForm;

    // ========== ACTION METHODS ==========

    /** Step 2: Click on “First-time Purchase” */
    public void goToFirstTime() {
        waitForClickable(driver, firstTimePurchaseButton).click();
    }

    /** Step 3: Select a random continent */
    public void selectRandomContinent() {
        if (continentOptions == null || continentOptions.isEmpty()) {
            throw new RuntimeException("No continent options found on the page");
        }

        int idx = (int) (Math.random() * continentOptions.size());
        WebElement selectedContinent = continentOptions.get(idx);
        String continentName = selectedContinent.getText().trim();

        waitForClickable(driver, selectedContinent).click();
        AllureStepLogger.logStep(getDriver(), "Selected continent: " + continentName);

        // Check if Antarctica was selected (based on the text or popup)
        if (continentName.contains("אנטארקטיקה") || continentName.equalsIgnoreCase("Antarctica")) {
            handleAntarcticaWarning();
        }
    }

    /**
     * Selects a specific continent by name.
     * Used for targeted testing (e.g., Antarctica warning popup).
     */
    public void selectRandomContinent(String continentName) {
        if (continentOptions == null || continentOptions.isEmpty()) {
            throw new RuntimeException("No continent options found on the page");
        }

        boolean found = false;
        for (WebElement continent : continentOptions) {
            String name = continent.getText().trim();
            if (name.equalsIgnoreCase(continentName) || name.contains(continentName)) {
                waitForClickable(driver, continent).click();
                AllureStepLogger.logStep(getDriver(), "Selected continent by name: " + name);
                found = true;

                // Special case: Antarctica triggers warning
                if (name.contains("אנטארקטיקה") || name.equalsIgnoreCase("Antarctica")) {
                    handleAntarcticaWarning();
                }
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Continent '" + continentName + "' not found among available options.");
        }
    }

    /**
     * Handles the Antarctica warning popup.
     * Waits for the "הבנתי" button and clicks it to dismiss.
     */
    private void handleAntarcticaWarning() {
        By understoodButton = By.xpath("//span[contains(text(),'הבנתי')]");
        BasePage.clickIfVisible(driver, understoodButton);
        AllureStepLogger.logStep(getDriver(), "Handled Antarctica warning (clicked 'הבנתי' if present)");
    }

    /** Step 4: Click “Next to travel dates” */
    public void clickNextToTravelDates() {
        waitForClickable(driver, nextToTravelDatesButton).click();
    }

    /** Step 5: Select departure (7 days from now) and return date (30 days after departure) */
    public void selectTravelDates() {
        LocalDate departure = LocalDate.now().plusDays(7);
        LocalDate returnDate = departure.plusDays(29); // The total-days counter includes the departure date.

        // Open departure date picker
        waitForClickable(driver, departureDateInput).click();
        selectDateFromPicker(departure);

        // Open return date picker
        waitForClickable(driver, returnDateInput).click();
        selectDateFromPicker(returnDate);
    }

    /**
     * Selects a given LocalDate from a date picker calendar.
     * Works with clickable day cells like <td data-day="7"> or similar.
     */
    private void selectDateFromPicker(LocalDate dateToSelect) {
        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("he")); // Hebrew month names
        String targetMonthYear = dateToSelect.format(headerFormatter);
        String dateAttribute = dateToSelect.toString(); // e.g., 2025-11-07

        try {
            // Wait for the date picker to appear
            WaitUtils.waitForVisibility(driver, By.cssSelector(".MuiPickersBasePicker-pickerView"));

            // Navigate months until correct header is visible
            while (true) {
                WebElement monthHeader = driver.findElement(By.cssSelector(".MuiPickersCalendarHeader-transitionContainer p"));
                String visibleMonth = monthHeader.getText().trim();

                if (visibleMonth.equalsIgnoreCase(targetMonthYear)) {
                    break;
                } else {
                    WebElement nextMonthButton = driver.findElement(
                            By.cssSelector("button[data-hrl-bo='arrow-forward'][data-hide='false']"));
                    waitForClickable(driver, nextMonthButton).click();
                    WaitUtils.shortSleep(400);
                }
            }

            // Once correct month visible, click the date
            By dateLocator = By.cssSelector(String.format("button[data-hrl-bo='%s']", dateAttribute));
            WebElement dateButton = WaitUtils.waitForClickable(driver, dateLocator);
            dateButton.click();

            AllureStepLogger.logStep(getDriver(), "Selected date from Date Picker: " + dateAttribute);

        } catch (NoSuchElementException e) {
            throw new RuntimeException("Could not find elements of the Date Picker.", e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Timeout while selecting date: " + dateToSelect, e);
        }
    }

    /** Step 6: Verify that total days label shows the correct duration (30 days)*/
    public boolean verifyTotalDays() {
        try {
            waitForVisibility(driver, totalDaysLabel);
            String text = totalDaysLabel.getText().trim();  // e.g. "סה\"כ: 30 ימים"

            AllureStepLogger.logStep(getDriver(), "Total days label text: " + text);

            return text.contains("30");

        } catch (Exception e) {
            AllureStepLogger.logStep(getDriver(), "Failed to verify total days label: " + e.getMessage());
            return false;
        }
    }

    /** Step 7: Click “Next to passenger details” */
    public void clickNextToPassengerDetails() {
        scrollToElement(driver, nextToPassengerDetailsButton);
        waitForClickable(driver, nextToPassengerDetailsButton).click();

    }

    /** Step 8: Verify passenger details page is displayed */
    public boolean isPassengerDetailsPageDisplayed() {
        try {
            waitForVisibility(driver, passengerDetailsForm);
            return passengerDetailsForm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // small helper
    private void shortWait() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
