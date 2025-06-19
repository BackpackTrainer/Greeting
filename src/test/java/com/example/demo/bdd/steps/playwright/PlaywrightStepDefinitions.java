package com.example.demo.bdd.steps.playwright;

import com.example.demo.bdd.util.TestDataCleaner;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.example.demo.bdd.util.EmojiStripper.stripEmojiPrefix;


public class PlaywrightStepDefinitions {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @Autowired
    private TestDataCleaner testDataCleaner;

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
        page.locator("[data-testid='get-all-members']").click();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        page.waitForCondition(() -> page.locator("[data-testid='member-row']").count() == expectedRows.size());

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            String actualText = page.locator("[data-testid='member-row']").nth(i).innerText();
            assertEquals(expectedText, actualText);
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        page.locator("button[data-testid='clear-display-button']").click();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        page.waitForCondition(() -> page.locator("[data-testid='member-row']").count() == 0);
        long count = page.locator("[data-testid='member-row']").count();
        assertEquals(0, count);
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterNameInTheFindGreetingByNameSearchField(String name) {
        Locator input = page.locator("[data-testid='find-greeting-input']");
        input.fill(name);
        Locator button = page.locator("[data-testid='find-greeting-button']");
        button.click();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        Locator message = page.locator("[data-testid='greeting-message']");
        message.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualText = message.innerText();
        String displayedGreeting = stripEmojiPrefix(actualText);
        assertEquals(expectedGreeting, displayedGreeting);
    }

    @Then("I should see this error message {string} in the results")
    public void iShouldSeeErrorMessage(String expectedErrorMessage) {
        Locator error = page.locator("[data-testid='greeting-error']");
        error.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualText = error.innerText();
        String displayedError = stripEmojiPrefix(actualText);
        assertEquals(expectedErrorMessage, displayedError);
    }

    // 🔥 NEW: Missing step added
    @When("I enter {string} and {string} in the Add Greeting form")
    public void iEnterNameAndGreetingInTheAddGreetingForm(String name, String greeting) {
        Locator nameInput = page.locator("[data-testid='add-name-input']");
        Locator greetingInput = page.locator("[data-testid='add-greeting-input']");
        Locator addButton = page.locator("[data-testid='add-greeting-button']");

        nameInput.fill(name);
        greetingInput.fill(greeting);
        addButton.click();
    }

    // 🔥 NEW: Missing step added
    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedSuccessMessage) {
        Locator result = page.locator("[data-testid='add-result-message']");
        result.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualText = result.innerText();
        String displayedMessage = stripEmojiPrefix(actualText);
        assertEquals(expectedSuccessMessage, displayedMessage);
    }

    // 🔥 NEW: Missing step added (this is the second variant you have in Selenium)
    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        Locator input = page.locator("[data-testid='find-greeting-input']");
        input.fill(name);
        Locator button = page.locator("[data-testid='find-greeting-button']");
        button.click();
    }
}