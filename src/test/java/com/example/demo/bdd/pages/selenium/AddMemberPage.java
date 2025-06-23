package com.example.demo.bdd.pages.selenium;

import com.example.demo.bdd.util.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddMemberPage {
    private final WebDriver driver;

    private final By NAME_INPUT = By.cssSelector("[data-testid='add-name-input']");
    private final By GREETING_INPUT = By.cssSelector("[data-testid='add-greeting-input']");
    private final By ADD_BUTTON = By.cssSelector("[data-testid='add-greeting-button']");
    private final By SUCCESS_MESSAGE = By.cssSelector("[data-testid='add-result-message']");
    private final By ERROR_MESSAGE = By.cssSelector("[data-testid='add-error-message']");

    public AddMemberPage(WebDriver driver) {
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
        driver.findElement(ADD_BUTTON).click();
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
