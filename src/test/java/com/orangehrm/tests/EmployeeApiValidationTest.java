package com.orangehrm.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class EmployeeApiValidationTest {
    @Test
    public void employeeRecordMatchesAuthenticatedApi() {
        String baseUrl = System.getProperty("orangehrm.baseUrl", "https://opensource-demo.orangehrmlive.com");
        String username = required("orangehrm.username");
        String password = required("orangehrm.password");
        int empNumber = Integer.parseInt(required("orangehrm.empNumber"));

        WebDriver driver = new ChromeDriver();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            driver.get(baseUrl + "/web/index.php/auth/login");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            wait.until(ExpectedConditions.urlContains("/dashboard/"));

            driver.get(baseUrl + "/web/index.php/pim/viewPersonalDetails/empNumber/" + empNumber);
            String firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")))
                    .getAttribute("value");
            String lastName = driver.findElement(By.name("lastName")).getAttribute("value");
            String employeeId = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.oxd-input[name='employeeId']"))).getAttribute("value");

            Assert.assertFalse(firstName.isBlank(), "UI first name is empty");
            Assert.assertFalse(lastName.isBlank(), "UI last name is empty");
            Assert.assertFalse(employeeId.isBlank(), "UI employee ID is empty");
            OrangeHrmEmployeeApiVerifier.assertEmployee(
                    driver, baseUrl, empNumber, employeeId, firstName, lastName);
        } finally {
            driver.quit();
        }
    }

    private static String required(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required Maven property: -D" + key + "=...");
        }
        return value;
    }
}
