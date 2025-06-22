package com.example.demo.bdd.steps.selenium;

import com.example.demo.bdd.util.ScreenshotUtility;
import com.example.demo.bdd.util.SeleniumElementUtils;
import com.example.demo.bdd.util.TestDataCleaner;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.demo.bdd.util.WaitUtility.waitForElementCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeleniumStepDefinitions {

    private WebDriver driver;

    @Autowired
    private TestDataCleaner testDataCleaner;

    // Centralized selectors
    private static final By GET_ALL_MEMBERS_BUTTON = By.cssSelector("[data-testid='get-all-members']");
    private static final By MEMBER_ROWS = By.cssSelector("[data-testid='member-row']");
    private static final By CLEAR_BUTTON = By.cssSelector("button[data-testid='clear-display-button']");
    private static final By FIND_NAME_INPUT = By.cssSelector("[data-testid='find-greeting-input']");
    private static final By FIND_BUTTON = By.cssSelector("[data-testid='find-greeting-button']");
    private static final By ADD_NAME_INPUT = By.cssSelector("[data-testid='add-name-input']");
    private static final By ADD_GREETING_INPUT = By.cssSelector("[data-testid='add-greeting-input']");
    private static final By ADD_BUTTON = By.cssSelector("[data-testid='add-greeting-button']");
    private static final By UPDATE_NAME_INPUT = By.cssSelector("[data-testid='update-name-input']");
    private static final By UPDATE_GREETING_INPUT = By.cssSelector("[data-testid='update-greeting-input']");
    private static final By UPDATE_BUTTON = By.cssSelector("[data-testid='update-greeting-button']");

    // ===== Test lifecycle hooks =====

    @Before(order = 1)
    public void resetDatabaseBeforeEachScenario() {
        testDataCleaner.resetTestData();
    }

    @Before(order = 2)
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

    // ===== Step definitions =====

    @Given("I have a browser open")
    public void i_have_a_browser_open() {
        assertNotNull(driver, "Browser should be initialized");
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
        driver.findElement(GET_ALL_MEMBERS_BUTTON).click();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        waitForElementCount(driver, MEMBER_ROWS, expectedRows.size());

        List<WebElement> rows = driver.findElements(MEMBER_ROWS);
        assertEquals(expectedRows.size(), rows.size());

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            assertEquals(expectedText, rows.get(i).getText());
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        driver.findElement(CLEAR_BUTTON).click();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        waitForElementCount(driver, MEMBER_ROWS, 0);
        int count = driver.findElements(MEMBER_ROWS).size();
        assertEquals(0, count);
    }

    @When("I enter {string} and {string} in the Add a New Member form")
    public void iEnterNameAndGreetingInTheAddANewMemberForm(String name, String greeting) {
        driver.findElement(ADD_NAME_INPUT).clear();
        driver.findElement(ADD_NAME_INPUT).sendKeys(name);
        driver.findElement(ADD_GREETING_INPUT).clear();
        driver.findElement(ADD_GREETING_INPUT).sendKeys(greeting);
        driver.findElement(ADD_BUTTON).click();
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedMessage) {
        SeleniumElementUtils.verifyDisplayedMessage(driver, expectedMessage);
    }

    @Then("I should see the failure message {string}")
    public void iShouldSeeTheFailureMessage(String expectedMessage) {
        SeleniumElementUtils.verifyComponentErrorMessage(driver, "add", expectedMessage);
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterNameInTheFindGreetingByNameSearchField(String name) {
        driver.findElement(FIND_NAME_INPUT).clear();
        driver.findElement(FIND_NAME_INPUT).sendKeys(name);
        driver.findElement(FIND_BUTTON).click();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        SeleniumElementUtils.verifyGreetingMessage(driver, expectedGreeting);
    }

    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        driver.findElement(FIND_NAME_INPUT).clear();
        driver.findElement(FIND_NAME_INPUT).sendKeys(name);
        driver.findElement(FIND_BUTTON).click();
    }

    @When("I enter {string} and {string} in the Updating Greeting form")
    public void iEnterNameAndGreetingInTheUpdatingGreetingForm(String name, String greeting) {
        driver.findElement(UPDATE_NAME_INPUT).clear();
        driver.findElement(UPDATE_NAME_INPUT).sendKeys(name);
        driver.findElement(UPDATE_GREETING_INPUT).clear();
        driver.findElement(UPDATE_GREETING_INPUT).sendKeys(greeting);
        driver.findElement(UPDATE_BUTTON).click();
    }

    @Then("I should see the greeting successfully updated message {string}")
    public void iShouldSeeTheGreetingSuccessfullyUpdatedMessage(String expectedMessage) {
        SeleniumElementUtils.verifyDisplayedMessage(driver, expectedMessage);
    }

    @Then("I should see the greeting failed to update message {string}")
    public void iShouldSeeTheGreetingFailedToUpdateMessage(String expectedMessage) {
        SeleniumElementUtils.verifyComponentErrorMessage(driver, "update", expectedMessage);
    }

    @Then("I should see the greeting {string} for {string}")
    public void iShouldSeeTheGreetingForName(String expectedGreeting, String name) {
        SeleniumElementUtils.verifyGreetingMessage(driver, expectedGreeting);
    }

    @Then("I should see the {string} error message {string}")
    public void iShouldSeeComponentErrorMessage(String component, String expectedErrorMessage) {
        SeleniumElementUtils.verifyComponentErrorMessage(driver, component, expectedErrorMessage);
    }

    @Then("I capture a screenshot named {string}")
    public void captureScreenshot(String screenshotName) throws IOException {
        ScreenshotUtility.takeScreenshotWithSelenium(driver, screenshotName);
    }
}
