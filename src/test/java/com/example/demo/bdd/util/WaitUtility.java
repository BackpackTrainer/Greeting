package com.example.demo.bdd.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtility {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    public static void waitForElementCount(WebDriver driver, By locator, int minCount) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, minCount - 1));
    }

    public static void waitForVisibility(WebDriver driver, By locator) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForInvisibility(WebDriver driver, By locator) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForTextToBePresent(WebDriver driver, By locator, String text) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
