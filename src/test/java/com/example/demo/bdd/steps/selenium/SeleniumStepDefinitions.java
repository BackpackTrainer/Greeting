package com.example.demo.bdd.steps.selenium;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Map;

import static com.example.demo.bdd.util.WaitUtility.waitForElementCount;
import static com.example.demo.bdd.util.EmojiStripper.stripEmojiPrefix;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumStepDefinitions {

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I have a browser open")
    public void i_have_a_browser_open() {
        Assertions.assertNotNull(driver, "Browser should be initialized");
    }

    @When("I enter the url {string}")
    public void i_enter_the_url(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        driver.get(url);
    }

    @Then("I am on the {string} page")
    public void i_am_on_the_page(String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle, "Page title mismatch");
    }

    @When("I request get all members")
    public void iRequestGetAllMembers() {
        // This simulates a user requesting all members
        WebElement getAllBtn = driver.findElement(By.cssSelector("[data-testid='get-all-members']"));
        getAllBtn.click();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);

        waitForElementCount(driver, By.cssSelector("[data-testid='member-row']"), expectedRows.size());

        List<WebElement> rows = driver.findElements(By.cssSelector("[data-testid='member-row']"));
        assertEquals(expectedRows.size(), rows.size());

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            assertEquals(expectedText, rows.get(i).getText());
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        WebElement clearButton = driver.findElement(By.cssSelector("button[data-testid='clear-display-button']"));
        clearButton.click();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        waitForElementCount(driver, By.cssSelector("[data-testid='member-row']"), 0);
        int count = driver.findElements(By.cssSelector("[data-testid='member-row']")).size();
        assertEquals(0, count);
    }

    @And("I enter {string} in the Find Greeting by name search field")
    public void iEnterNameInTheFindGreetingByNameSearchField(String name) {
        WebElement input = driver.findElement(By.cssSelector("[data-testid='find-greeting-input']"));
        input.clear();  // just in case something is already entered
        input.sendKeys(name);

        WebElement button = driver.findElement(By.cssSelector("[data-testid='find-greeting-button']"));
        button.click();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        // Wait until the greeting message is visible
        waitForElementCount(driver, By.cssSelector("[data-testid='greeting-message']"), 1);

        WebElement message = driver.findElement(By.cssSelector("[data-testid='greeting-message']"));
        String displayedGreeting = message.getText();

        // Strip off the 💬 if you don't want to include it in the feature file
        //String displayedGreeting = actualText.replace("💬 ", "").trim();
        String actualGreeting = stripEmojiPrefix(displayedGreeting);
        assertEquals(expectedGreeting, actualGreeting);
    }

    @Then("I should see this error message {string} in the results")
    public void iShouldSeeErrorMessgaeInTheResults(String expectedGreeting) {
        // Wait until the greeting message is visible
        waitForElementCount(driver, By.cssSelector("[data-testid='greeting-error']"), 1);

        WebElement message = driver.findElement(By.cssSelector("[data-testid='greeting-error']"));
        String actualText = message.getText();

        // Strip off the 💬 if you don't want to include it in the feature file
        String displayedError = stripEmojiPrefix(actualText);
        assertEquals(expectedGreeting, displayedError);
    }
}

