package com.example.demo.bdd.pages.selenium;

import com.example.demo.bdd.util.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FindGreetingPage {

    private WebDriver driver;

    // Locators
    private static final By NAME_INPUT = By.cssSelector("[data-testid='find-greeting-input']");
    private static final By SUBMIT_BUTTON = By.cssSelector("[data-testid='find-greeting-button']");
    private static final By FIND_RESULT_MESSAGE = By.cssSelector("[data-testid='find-result-message']");
    private static final By FIND_ERROR_MESSAGE = By.cssSelector("[data-testid='find-error-message']");

    public FindGreetingPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterName(String name) {
        driver.findElement(NAME_INPUT).clear();
        driver.findElement(NAME_INPUT).sendKeys(name);
    }

    public void submit() {
        WaitUtility.waitUntilClickable(driver, SUBMIT_BUTTON);
        driver.findElement(SUBMIT_BUTTON).click();
        WaitUtility.waitUntilOneIsVisible(driver, FIND_RESULT_MESSAGE, FIND_ERROR_MESSAGE);
    }

    public String getGreetingMessage() {
        WaitUtility.waitUntilVisible(driver, FIND_RESULT_MESSAGE);
        return driver.findElement(FIND_RESULT_MESSAGE).getText();
    }

    public String getErrorMessage() {
        WaitUtility.waitUntilVisible(driver, FIND_ERROR_MESSAGE);
        return driver.findElement(FIND_ERROR_MESSAGE).getText();
    }
}
