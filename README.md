# 🧭 Harel Travel Policy Automation Framework

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.20-brightgreen?logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-orange)
![Allure](https://img.shields.io/badge/Allure-Report-purple?logo=allure)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## 📘 Overview
End-to-end **UI Automation Framework** for testing the Harel Insurance Travel Policy site:  
🔗 [https://digital.harel-group.co.il/travel-policy](https://digital.harel-group.co.il/travel-policy)

Built with **Java + Selenium + TestNG + Allure**, this framework ensures reliability, modularity, and full reporting coverage.

---

## 🚀 Key Features
- Selenium WebDriver & TestNG integration  
- Allure reports with screenshots on failure  
- Retry mechanism for flaky tests  
- Explicit & implicit waits (WaitUtils)  
- Dynamic Date Picker selection  
- Auto environment logging  
- Cross-browser ready  

---

## 🧠 Test Flow
✅ Navigate to base URL  
✅ Click “לרכישה בפעם הראשונה”  
✅ Choose a random continent  
✅ Select travel dates (7 → 30 days) via Date Picker  
✅ Verify total days = 30  
✅ Proceed to passenger details  
✅ Verify traveler form loads  

---

## 🧩 Project Structure
```
src/
 ├── main/java/com/eliranduveen/
 │   ├── base/       # Base classes
 │   ├── config/     # ConfigManager & properties
 │   ├── drivers/    # DriverFactory
 │   ├── pages/      # Page Objects
 │   └── utils/      # Helpers (Wait, Retry, Allure, Screenshot)
 └── test/java/com/eliranduveen/tests/
     └── FirstTimePurchaseTest.java
```

---

## ⚙️ Run Tests with Reports
### Run Test:
```bash
mvn clean test
```
### Generate Allure report:
```bash
mvn io.qameta.allure:allure-maven:report
```
### Run Test & Generate Allure report:
```bash
mvn clean verify
```
### Generate & Open Report with Local Server:
```bash
mvn io.qameta.allure:allure-maven:serve
```



---


## 📊 Reports
Allure generates:
- Step logs & screenshots  
- Browser/environment metadata  
- Failure analysis  

## Output folders:
```
target/allure-results/
target/allure-report/
```
## Report Path
```
target/allure-report/index.html
```
---

## 📸 Automatic Screenshot Capture on Test Failure
When a test fails, the framework automatically captures a screenshot and attaches it to the Allure report for easy debugging.
This feature is implemented in the @AfterMethod hook and uses a dedicated utility class for consistency and reusability.
```java
@AfterMethod(alwaysRun = true)
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        ScreenshotUtils.attachScreenshot(DriverFactory.getDriver(), result.getName());
    }
    DriverFactory.quitDriver();
}
```

---

## 🔄 Retry Mechanism
Retries failed tests up to 2 times:
```java
@Test(retryAnalyzer = com.eliranduveen.utils.RetryAnalyzer.class)
```

---

## 🧱 Tech Stack
**Language:** Java 17  
**Frameworks:** Selenium, TestNG, Allure  
**Build Tool:** Maven  
**Reports:** Allure HTML Report  

---

## 🧹 Future Roadmap
- Dockerized Selenium Grid  
- CI/CD pipeline (Jenkins / GitHub Actions)  
- Cross-browser execution  
- Parallel testing support  

---

🧑‍💻 **Author:** Eliran Duveen  
📂 **License:** MIT  
