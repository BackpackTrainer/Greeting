package com.example.demo.bdd.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.example.demo.bdd.util.WaitUtility.waitUntilTextContains;

public class SeleniumElementUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 5;

    // For success or failure messages displayed in Add or Update forms
    public static void verifyDisplayedMessage(WebDriver driver, String expectedMessage) {
        By locator = By.cssSelector("[data-testid='add-result-message']");
        waitUntilTextContains(driver, locator, expectedMessage, DEFAULT_TIMEOUT_SECONDS);
    }

    // For general component-specific error messages (parameterized)
    public static void verifyComponentErrorMessage(WebDriver driver, String component, String expectedMessage) {
        By locator = By.cssSelector("[data-testid='" + component + "-error-message']");
        waitUntilTextContains(driver, locator, expectedMessage, DEFAULT_TIMEOUT_SECONDS);
    }

    // For greeting lookup results (in Find Greeting by Name form)
    public static void verifyGreetingMessage(WebDriver driver, String expectedMessage) {
        By locator = By.cssSelector("[data-testid='greeting-message']");
        waitUntilTextContains(driver, locator, expectedMessage, DEFAULT_TIMEOUT_SECONDS);
    }
}
