package com.example.demo.bdd.pages.selenium;

import com.example.demo.bdd.util.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UpdateGreetingPage {
    private final WebDriver driver;

    private final By NAME_INPUT = By.cssSelector("[data-testid='update-name-input']");
    private final By GREETING_INPUT = By.cssSelector("[data-testid='update-greeting-input']");
    private final By UPDATE_BUTTON = By.cssSelector("[data-testid='update-greeting-button']");
    private final By SUCCESS_MESSAGE = By.cssSelector("[data-testid='update-result-message']");
    private final By ERROR_MESSAGE = By.cssSelector("[data-testid='update-error-message']");

    public UpdateGreetingPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterName(String name) {
        driver.findElement(NAME_INPUT).clear();
        driver.findElement(NAME_INPUT).sendKeys(name);
    }

    public void enterGreeting(String greeting) {
        driver.findElement(GREETING_INPUT).clear();
        driver.findElement(GREETING_INPUT).sendKeys(greeting);
    }

    public void submit() {
        driver.findElement(UPDATE_BUTTON).click();
        WaitUtility.waitUntilOneIsVisible(driver, SUCCESS_MESSAGE, ERROR_MESSAGE);
    }

    public String getSuccessMessage() {
        WaitUtility.waitUntilVisible(driver, SUCCESS_MESSAGE);
        return driver.findElement(SUCCESS_MESSAGE).getText();
    }

    public String getErrorMessage() {
        WaitUtility.waitUntilVisible(driver, ERROR_MESSAGE);
        return driver.findElement(ERROR_MESSAGE).getText();
    }
}
