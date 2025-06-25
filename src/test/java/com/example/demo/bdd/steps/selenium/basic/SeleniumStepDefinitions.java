// -----------------------------
// Selenium Step Definitions (UPDATED WITH WAIT UTILITY)
// -----------------------------

package com.example.demo.bdd.steps.selenium.basic;

import com.example.demo.bdd.util.ScreenshotUtility;
import com.example.demo.bdd.util.TestDataCleaner;
import com.example.demo.bdd.util.WaitUtility;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumStepDefinitions {

    private WebDriver driver;

    @Autowired
    private TestDataCleaner testDataCleaner;

    private static final By GET_ALL_MEMBERS_BTN = By.cssSelector("[data-testid='get-all-members']");
    private static final By CLEAR_BUTTON = By.cssSelector("[data-testid='clear-display-button']");
    private static final By MEMBER_ROW = By.cssSelector("[data-testid='member-row']");
    private static final By FIND_GREETING_INPUT = By.cssSelector("[data-testid='find-greeting-input']");
    private static final By FIND_GREETING_BUTTON = By.cssSelector("[data-testid='find-greeting-button']");
    private static final By ADD_NAME_INPUT = By.cssSelector("[data-testid='add-name-input']");
    private static final By ADD_GREETING_INPUT = By.cssSelector("[data-testid='add-greeting-input']");
    private static final By ADD_GREETING_BUTTON = By.cssSelector("[data-testid='add-greeting-button']");
    private static final By UPDATE_NAME_INPUT = By.cssSelector("[data-testid='update-name-input']");
    private static final By UPDATE_GREETING_INPUT = By.cssSelector("[data-testid='update-greeting-input']");
    private static final By UPDATE_GREETING_BUTTON = By.cssSelector("[data-testid='update-greeting-button']");

    private static final By RESULT_MESSAGE = By.cssSelector("[data-testid='result-message']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-testid='error-message']");

    @Before(order = 1)
    public void resetDatabaseBeforeEachScenario() {
        testDataCleaner.resetTestData();
    }

    @Before(order = 2)
    public void setUp() {
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
        Assertions.assertNotNull(driver);
    }

    @When("I enter the url {string}")
    public void i_enter_the_url(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        driver.get(url);
    }

    @Then("I am on the {string} page")
    public void iAmOnThePage(String expectedTitle) {
        assertEquals(expectedTitle, driver.getTitle());
    }

    @When("I request get all members")
    public void iRequestGetAllMembers() {
        WaitUtility.waitUntilClickable(driver, GET_ALL_MEMBERS_BTN);
        driver.findElement(GET_ALL_MEMBERS_BTN).click();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        WaitUtility.waitForElementCount(driver, MEMBER_ROW, expectedRows.size());
        List<WebElement> actualRows = driver.findElements(MEMBER_ROW);
        assertEquals(expectedRows.size(), actualRows.size());
        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            assertEquals(expectedText, actualRows.get(i).getText());
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        WaitUtility.waitUntilClickable(driver, CLEAR_BUTTON);
        driver.findElement(CLEAR_BUTTON).click();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        WaitUtility.waitForElementCount(driver, MEMBER_ROW, 0);
        List<WebElement> members = driver.findElements(MEMBER_ROW);
        assertEquals(0, members.size());
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterNameInTheFindGreetingByNameSearchField(String name) {
        WaitUtility.waitUntilClickable(driver, FIND_GREETING_INPUT);
        driver.findElement(FIND_GREETING_INPUT).clear();
        driver.findElement(FIND_GREETING_INPUT).sendKeys(name);
        driver.findElement(FIND_GREETING_BUTTON).click();
    }

    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        WaitUtility.waitUntilClickable(driver, FIND_GREETING_INPUT);
        driver.findElement(FIND_GREETING_INPUT).clear();
        driver.findElement(FIND_GREETING_INPUT).sendKeys(name);
        driver.findElement(FIND_GREETING_BUTTON).click();
    }

    @When("I enter {string} and {string} in the Add a New Member form")
    public void iEnterNameAndGreetingInTheAddANewMemberForm(String name, String greeting) {
        WaitUtility.waitUntilClickable(driver, ADD_NAME_INPUT);
        driver.findElement(ADD_NAME_INPUT).clear();
        driver.findElement(ADD_NAME_INPUT).sendKeys(name);
        driver.findElement(ADD_GREETING_INPUT).clear();
        driver.findElement(ADD_GREETING_INPUT).sendKeys(greeting);
        driver.findElement(ADD_GREETING_BUTTON).click();
    }

    @When("I enter {string} and {string} in the Updating Greeting form")
    public void iEnterNameAndGreetingInTheUpdatingGreetingForm(String name, String greeting) {
        WaitUtility.waitUntilClickable(driver, UPDATE_NAME_INPUT);
        driver.findElement(UPDATE_NAME_INPUT).clear();
        driver.findElement(UPDATE_NAME_INPUT).sendKeys(name);
        driver.findElement(UPDATE_GREETING_INPUT).clear();
        driver.findElement(UPDATE_GREETING_INPUT).sendKeys(greeting);
        driver.findElement(UPDATE_GREETING_BUTTON).click();
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedMessage) {
        WaitUtility.waitUntilTextContains(driver, RESULT_MESSAGE, expectedMessage);
        WebElement element = driver.findElement(RESULT_MESSAGE);
        assertEquals(expectedMessage, element.getText());
    }

    @Then("I should see the greeting successfully updated message {string}")
    public void iShouldSeeTheGreetingSuccessfullyUpdatedMessage(String expectedMessage) {
        WaitUtility.waitUntilTextContains(driver, RESULT_MESSAGE, expectedMessage);
        WebElement element = driver.findElement(RESULT_MESSAGE);
        assertEquals(expectedMessage, element.getText());
    }

    @Then("I should see the failure message {string}")
    public void iShouldSeeTheFailureMessage(String expectedMessage) {
        WaitUtility.waitUntilTextContains(driver, ERROR_MESSAGE, expectedMessage);
        WebElement element = driver.findElement(ERROR_MESSAGE);
        assertEquals(expectedMessage, element.getText());
    }

    @Then("I should see the greeting failed to update message {string}")
    public void iShouldSeeTheGreetingFailedToUpdateMessage(String expectedMessage) {
        WaitUtility.waitUntilTextContains(driver, ERROR_MESSAGE, expectedMessage);
        WebElement element = driver.findElement(ERROR_MESSAGE);
        assertEquals(expectedMessage, element.getText());
    }

    @Then("I should see the {string} error message {string}")
    public void iShouldSeeComponentErrorMessage(String component, String expectedErrorMessage) {
        WaitUtility.waitUntilTextContains(driver, ERROR_MESSAGE, expectedErrorMessage);
        WebElement element = driver.findElement(ERROR_MESSAGE);
        assertEquals(expectedErrorMessage, element.getText());
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        WaitUtility.waitUntilTextContains(driver, RESULT_MESSAGE, expectedGreeting);
        WebElement element = driver.findElement(RESULT_MESSAGE);
        assertEquals(expectedGreeting, element.getText());
    }

    @Then("I should see the greeting {string} for {string}")
    public void iShouldSeeTheGreetingForName(String expectedGreeting, String name) {
        WaitUtility.waitUntilTextContains(driver, RESULT_MESSAGE, expectedGreeting);
        WebElement element = driver.findElement(RESULT_MESSAGE);
        assertEquals(expectedGreeting, element.getText());
    }

    @Then("I capture a screenshot named {string}")
    public void captureScreenshot(String screenshotName) throws IOException {
        ScreenshotUtility.takeScreenshotWithSelenium(driver, screenshotName);
    }
}