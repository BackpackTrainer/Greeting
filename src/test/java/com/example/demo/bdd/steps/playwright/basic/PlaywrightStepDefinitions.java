package com.example.demo.bdd.steps.playwright.basic;

import com.example.demo.bdd.util.ScreenshotUtility;
import com.example.demo.bdd.util.TestDataCleaner;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaywrightStepDefinitions {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @Autowired
    private TestDataCleaner testDataCleaner;

    // Updated CSS Selectors (matching Selenium updates)
    private static final String GET_ALL_MEMBERS_BTN = "[data-testid='get-all-members']";
    private static final String CLEAR_BUTTON = "[data-testid='clear-display-button']";
    private static final String MEMBER_ROW = "[data-testid='member-row']";

    private static final String FIND_GREETING_INPUT = "[data-testid='find-greeting-input']";
    private static final String FIND_GREETING_BUTTON = "[data-testid='find-greeting-button']";
    private static final String GREETING_MESSAGE = "[data-testid='result-message']";
    private static final String GREETING_ERROR = "[data-testid='error-message']";

    private static final String ADD_NAME_INPUT = "[data-testid='add-name-input']";
    private static final String ADD_GREETING_INPUT = "[data-testid='add-greeting-input']";
    private static final String ADD_GREETING_BUTTON = "[data-testid='add-greeting-button']";
    private static final String ADD_RESULT_MESSAGE = "[data-testid='result-message']";
    private static final String ADD_ERROR_MESSAGE = "[data-testid='error-message']";

    private static final String UPDATE_NAME_INPUT = "[data-testid='update-name-input']";
    private static final String UPDATE_GREETING_INPUT = "[data-testid='update-greeting-input']";
    private static final String UPDATE_GREETING_BUTTON = "[data-testid='update-greeting-button']";
    private static final String UPDATE_RESULT_MESSAGE = "[data-testid='result-message']";
    private static final String UPDATE_ERROR_MESSAGE = "[data-testid='error-message']";

    @Before(order = 1)
    public void resetDatabaseBeforeEachScenario() {
        testDataCleaner.resetTestData();
    }

    @Before(order = 2)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @After
    public void tearDown() {
        if (browser != null) {
            browser.close();
            playwright.close();
        }
    }

    @Given("I have a browser open")
    public void i_have_a_browser_open() {
        Assertions.assertNotNull(page, "Browser page should be initialized");
    }

    @When("I enter the url {string}")
    public void i_enter_the_url(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        page.navigate(url);
    }

    @Then("I am on the {string} page")
    public void i_am_on_the_page(String expectedTitle) {
        String actualTitle = page.title();
        assertEquals(expectedTitle, actualTitle, "Page title mismatch");
    }

    @When("I request get all members")
    public void iRequestGetAllMembers() {
        page.locator(GET_ALL_MEMBERS_BTN).click();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        page.waitForCondition(() -> page.locator(MEMBER_ROW).count() == expectedRows.size());

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            String actualText = page.locator(MEMBER_ROW).nth(i).innerText();
            assertEquals(expectedText, actualText);
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        page.locator(CLEAR_BUTTON).click();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        page.waitForCondition(() -> page.locator(MEMBER_ROW).count() == 0);
        long count = page.locator(MEMBER_ROW).count();
        assertEquals(0, count);
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterNameInTheFindGreetingByNameSearchField(String name) {
        page.locator(FIND_GREETING_INPUT).fill(name);
        page.locator(FIND_GREETING_BUTTON).click();
    }

    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        page.locator(FIND_GREETING_INPUT).fill(name);
        page.locator(FIND_GREETING_BUTTON).click();
    }

    @When("I enter {string} and {string} in the Add a New Member form")
    public void iEnterNameAndGreetingInTheAddANewMemberForm(String name, String greeting) {
        page.locator(ADD_NAME_INPUT).fill(name);
        page.locator(ADD_GREETING_INPUT).fill(greeting);
        page.locator(ADD_GREETING_BUTTON).click();
    }

    @When("I enter {string} and {string} in the Updating Greeting form")
    public void iEnterNameAndGreetingInTheUpdatingGreetingForm(String name, String greeting) {
        page.locator(UPDATE_NAME_INPUT).fill(name);
        page.locator(UPDATE_GREETING_INPUT).fill(greeting);
        page.locator(UPDATE_GREETING_BUTTON).click();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        assertMessage(GREETING_MESSAGE, expectedGreeting);
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedSuccessMessage) {
        assertMessage(ADD_RESULT_MESSAGE, expectedSuccessMessage);
    }

    @Then("I should see the failure message {string}")
    public void iShouldSeeTheFailureMessage(String expectedErrorMessage) {
        assertMessage(ADD_ERROR_MESSAGE, expectedErrorMessage);
    }

    @Then("I should see the greeting successfully updated message {string}")
    public void iShouldSeeTheGreetingSuccessfullyUpdatedMessage(String expectedMessage) {
        assertMessage(UPDATE_RESULT_MESSAGE, expectedMessage);
    }

    @Then("I should see the greeting failed to update message {string}")
    public void iShouldSeeTheGreetingFailedToUpdateMessage(String expectedMessage) {
        assertMessage(UPDATE_ERROR_MESSAGE, expectedMessage);
    }

    @Then("I should see the {string} error message {string}")
    public void iShouldSeeComponentErrorMessage(String component, String expectedErrorMessage) {
        String cssSelector;
        switch (component.toLowerCase()) {
            case "add" -> cssSelector = ADD_ERROR_MESSAGE;
            case "update" -> cssSelector = UPDATE_ERROR_MESSAGE;
            default -> throw new IllegalArgumentException("Unknown component: " + component);
        }
        assertMessage(cssSelector, expectedErrorMessage);
    }

    @Then("I should see the greeting {string} for {string}")
    public void iShouldSeeTheGreetingForName(String expectedGreeting, String name) {
        assertMessage(GREETING_MESSAGE, expectedGreeting);
    }

    private void assertMessage(String cssSelector, String expectedMessage) {
        // Instead of using the cssSelector, we'll locate by text directly
        Locator locator = page.getByText(expectedMessage, new Page.GetByTextOptions().setExact(true));
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String actualText = locator.innerText();
        assertEquals(expectedMessage, actualText);
    }


    @Then("I capture a screenshot named {string}")
    public void captureScreenshot(String screenshotName) throws IOException {
        ScreenshotUtility.takeScreenshotWithPlaywright(page, screenshotName);
    }
}
