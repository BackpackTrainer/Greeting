package com.example.demo.bdd.pages.selenium;

import com.example.demo.bdd.util.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MemberListPage {
    private final WebDriver driver;

    private final By GET_ALL_MEMBERS_BUTTON = By.cssSelector("[data-testid='get-all-members']");
    private final By CLEAR_BUTTON = By.cssSelector("[data-testid='clear-display-button']");
    private final By MEMBER_ROWS = By.cssSelector("[data-testid='member-row']");

    public MemberListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickGetAllMembers() {
        driver.findElement(GET_ALL_MEMBERS_BUTTON).click();
    }

    public void clickClearAll() {
        driver.findElement(CLEAR_BUTTON).click();
    }

    public List<WebElement> getMemberRows() {
        return driver.findElements(MEMBER_ROWS);
    }

    public void waitForMemberCount(int expectedCount) {
        WaitUtility.waitForElementCount(driver, MEMBER_ROWS, expectedCount);
    }
}
