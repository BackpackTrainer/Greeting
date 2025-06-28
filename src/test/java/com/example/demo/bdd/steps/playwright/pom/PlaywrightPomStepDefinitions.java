package com.example.demo.bdd.steps.playwright.pom;

import com.example.demo.bdd.pages.playwright.*;
import com.example.demo.bdd.util.ScreenshotUtility;
import com.example.demo.bdd.util.TestDataCleaner;
import com.microsoft.playwright.*;
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

public class PlaywrightPomStepDefinitions {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    private AddMemberPage addMemberPage;
    private UpdateGreetingPage updateGreetingPage;
    private FindGreetingPage findGreetingPage;
    private MemberListPage memberListPage;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @Before(order = 1)
    public void resetDatabase() {
        testDataCleaner.resetTestData();
    }

    @Before(order = 2)
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();

        // Initialize Page Objects
        addMemberPage = new AddMemberPage(page);
        updateGreetingPage = new UpdateGreetingPage(page);
        findGreetingPage = new FindGreetingPage(page);
        memberListPage = new MemberListPage(page);
    }

    @After
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    @Given("I have a browser open")
    public void i_have_a_browser_open() {
        Assertions.assertNotNull(page, "Browser should be open");
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
        assertEquals(expectedTitle, page.title());
    }

    @When("I request get all members")
    public void iRequestGetAllMembers() {
        memberListPage.clickGetAllMembers();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);

        // NEW: wait for members to be displayed before counting
        memberListPage.waitForMembersToBeDisplayed();

        long actualCount = memberListPage.getDisplayedMemberCount();
        assertEquals(expectedRows.size(), actualCount);

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            assertEquals(expectedText, memberListPage.getMemberRowText(i).trim());
        }
    }


    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        memberListPage.clickClearAll();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        long count = memberListPage.getDisplayedMemberCount();
        assertEquals(0, count);
    }

    @When("I enter {string} and {string} in the Add a New Member form")
    public void iEnterNameAndGreetingInTheAddANewMemberForm(String name, String greeting) {
        addMemberPage.enterName(name);
        addMemberPage.enterGreeting(greeting);
        addMemberPage.submit();
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedMessage) {
        assertEquals(expectedMessage, addMemberPage.getSuccessMessage().trim());
    }

    @Then("I should see the failure message {string}")
    public void iShouldSeeTheFailureMessage(String expectedMessage) {
        assertEquals(expectedMessage, addMemberPage.getErrorMessage().trim());
    }

    @When("I enter {string} and {string} in the Updating Greeting form")
    public void iEnterNameAndGreetingInTheUpdatingGreetingForm(String name, String greeting) {
        updateGreetingPage.enterName(name);
        updateGreetingPage.enterGreeting(greeting);
        updateGreetingPage.submit();
    }

    @Then("I should see the greeting successfully updated message {string}")
    public void iShouldSeeTheGreetingSuccessfullyUpdatedMessage(String expectedMessage) {
        assertEquals(expectedMessage, updateGreetingPage.getSuccessMessage().trim());
    }

    @Then("I should see the greeting failed to update message {string}")
    public void iShouldSeeTheGreetingFailedToUpdateMessage(String expectedMessage) {
        assertEquals(expectedMessage, updateGreetingPage.getErrorMessage().trim());
    }

    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        findGreetingPage.enterName(name);
        findGreetingPage.submit();
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterInTheFindGreetingByNameSearchField(String name) {
        findGreetingPage.enterName(name);
        findGreetingPage.submit();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        assertEquals(expectedGreeting, findGreetingPage.getGreetingMessage().trim());
    }

    @Then("I should see the greeting {string} for {string}")
    public void iShouldSeeGreetingForName(String expectedGreeting, String name) {
        assertEquals(expectedGreeting, findGreetingPage.getGreetingMessage().trim());
    }
    @Then("I should see the find error message {string}")
    public void iShouldSeeTheFindErrorMessage(String expectedMessage) {
        String actualMessage = findGreetingPage.getGreetingMessage();
        assertEquals(expectedMessage, actualMessage);
    }


    @Then("I should see the add error message {string}")
    public void i_should_see_the_add_error_message(String string) {
        String actualMessage = addMemberPage.getErrorMessage().trim();
        assertEquals(string, actualMessage);
    }


    @Then("I should see the {string} error message {string}")
    public void iShouldSeeComponentErrorMessage(String component, String expectedMessage) {
        String actual;
        if (component.equalsIgnoreCase("add")) {
            actual = addMemberPage.getErrorMessage().trim();
        } else if (component.equalsIgnoreCase("update")) {
            actual = updateGreetingPage.getErrorMessage().trim();
        } else {
            throw new IllegalArgumentException("Unknown component: " + component);
        }
        assertEquals(expectedMessage, actual);
    }

    @Then("I capture a screenshot named {string}")
    public void captureScreenshot(String screenshotName) throws IOException {
        ScreenshotUtility.takeScreenshotWithPlaywright(page, screenshotName);
    }
}
