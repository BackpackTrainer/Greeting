package com.example.demo.bdd.util;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtility {

    public static void waitUntilTextContains(WebDriver driver, By locator, String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
    }

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


    public static void waitUntilVisible(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementCount(WebDriver driver, By locator, int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until((WebDriver d) -> {
            List<WebElement> elements = d.findElements(locator);
            return elements.size() == expectedCount;
        });
    }
    public static void waitUntilClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }


    // 🆕 NEW METHOD:
    public static void waitUntilOneIsVisible(WebDriver driver, By... locators) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver1 -> {
            for (By locator : locators) {
                List<WebElement> elements = driver1.findElements(locator);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return true;
                }
            }
            return false;
        });
    }
}
