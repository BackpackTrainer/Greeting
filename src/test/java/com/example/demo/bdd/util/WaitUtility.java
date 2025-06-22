package com.example.demo.bdd.util;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtility {

    // Default text wait method (backward compatible)
    public static void waitUntilTextContains(WebDriver driver, By locator, String expectedText) {
        waitUntilTextContains(driver, locator, expectedText, 5); // default timeout 5 seconds
    }

    // Overloaded text wait with debug
    public static void waitUntilTextContains(WebDriver driver, By locator, String expectedText, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
        } catch (TimeoutException e) {
            System.out.println("===== DEBUG DUMP START =====");
            System.out.println("Expected text: '" + expectedText + "'");
            System.out.println("Actual page source:");
            System.out.println(driver.getPageSource());
            System.out.println("===== DEBUG DUMP END =====");
            throw e;
        }
    }

    // Element count wait (this was missing)
    public static void waitForElementCount(WebDriver driver, By locator, int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until((WebDriver d) -> {
            List<WebElement> elements = d.findElements(locator);
            return elements.size() == expectedCount;
        });
    }
}
