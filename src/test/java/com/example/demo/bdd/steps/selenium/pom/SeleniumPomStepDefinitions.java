package com.example.demo.bdd.steps.selenium.pom;

import com.example.demo.bdd.pages.selenium.*;
import com.example.demo.bdd.util.ScreenshotUtility;
import com.example.demo.bdd.util.TestDataCleaner;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeleniumPomStepDefinitions {

    private WebDriver driver;
    private AddMemberPage addMemberPage;
    private UpdateGreetingPage updateGreetingPage;
    private FindGreetingPage findGreetingPage;
    private MemberListPage memberListPage;
    private HomePage homePage;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @Before(order = 1)
    public void resetDatabaseBeforeEachScenario() {
        testDataCleaner.resetTestData();
    }

    @Before(order = 2)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        addMemberPage = new AddMemberPage(driver);
        updateGreetingPage = new UpdateGreetingPage(driver);
        findGreetingPage = new FindGreetingPage(driver);
        memberListPage = new MemberListPage(driver);
        homePage = new HomePage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

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
        String actualTitle = homePage.getPageTitle();
        assertEquals(expectedTitle, actualTitle, "Page title mismatch");
    }

    @When("I enter {string} and {string} in the Add a New Member form")
    public void iEnterNameAndGreetingInTheAddANewMemberForm(String name, String greeting) {
        addMemberPage.enterName(name);
        addMemberPage.enterGreeting(greeting);
        addMemberPage.submit();
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeTheSuccessMessage(String expectedMessage) {
        String actual = addMemberPage.getSuccessMessage();
        assertEquals(expectedMessage, actual);
    }

    @Then("I should see the failure message {string}")
    public void iShouldSeeTheFailureMessage(String expectedMessage) {
        String actual = addMemberPage.getErrorMessage();
        assertEquals(expectedMessage, actual);
    }

    @When("I enter {string} and {string} in the Updating Greeting form")
    public void iEnterNameAndGreetingInTheUpdatingGreetingForm(String name, String greeting) {
        updateGreetingPage.enterName(name);
        updateGreetingPage.enterGreeting(greeting);
        updateGreetingPage.submit();
    }

    @Then("I should see the greeting successfully updated message {string}")
    public void iShouldSeeTheGreetingSuccessfullyUpdatedMessage(String expectedMessage) {
        String actual = updateGreetingPage.getSuccessMessage();
        assertEquals(expectedMessage, actual);
    }

    @Then("I should see the greeting failed to update message {string}")
    public void iShouldSeeTheGreetingFailedToUpdateMessage(String expectedMessage) {
        String actual = updateGreetingPage.getErrorMessage();
        assertEquals(expectedMessage, actual);
    }

    @When("I search for {string} in the Find Greeting by Name form")
    public void iSearchForNameInTheFindGreetingByNameForm(String name) {
        findGreetingPage.enterName(name);
        findGreetingPage.submit();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeGreetingInTheResults(String expectedGreeting) {
        String actual = findGreetingPage.getGreetingMessage();
        assertEquals(expectedGreeting, actual);
    }

    @Then("I should see the {string} error message {string}")
    public void iShouldSeeTheComponentErrorMessage(String component, String expectedErrorMessage) {
        String actual;
        if (component.equalsIgnoreCase("add")) {
            actual = addMemberPage.getErrorMessage();
        } else if (component.equalsIgnoreCase("update")) {
            actual = updateGreetingPage.getErrorMessage();
        } else {
            throw new IllegalArgumentException("Unknown component: " + component);
        }
        assertEquals(expectedErrorMessage, actual);
    }

    @When("I request get all members")
    public void iRequestGetAllMembers() {
        memberListPage.clickGetAllMembers();
    }

    @Then("the following members should be returned:")
    public void theFollowingMembersShouldBeReturned(DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        memberListPage.waitForMemberCount(expectedRows.size());

        List<WebElement> actualRows = memberListPage.getMemberRows();
        assertEquals(expectedRows.size(), actualRows.size());

        for (int i = 0; i < expectedRows.size(); i++) {
            String expectedText = expectedRows.get(i).get("name") + ": " + expectedRows.get(i).get("greeting");
            assertEquals(expectedText, actualRows.get(i).getText());
        }
    }

    @When("I request clear all members")
    public void iRequestClearAllMembers() {
        memberListPage.clickClearAll();
    }

    @Then("no members will be displayed")
    public void noMembersWillBeDisplayed() {
        memberListPage.waitForMemberCount(0);
        List<WebElement> rows = memberListPage.getMemberRows();
        assertEquals(0, rows.size());
    }

    @When("I enter {string} in the Find Greeting by name search field")
    public void iEnterInTheFindGreetingByNameSearchField(String name) {
        findGreetingPage.enterName(name);
        findGreetingPage.submit();
    }

    @Then("I should see the greeting {string} for {string}")
    public void iShouldSeeTheGreetingForName(String expectedGreeting, String name) {
        String actualMessage = findGreetingPage.getGreetingMessage();
        assertEquals(expectedGreeting, actualMessage, "Greeting message mismatch for name: " + name);
    }

    @Then("I capture a screenshot named {string}")
    public void captureScreenshot(String screenshotName) throws IOException {
        ScreenshotUtility.takeScreenshotWithSelenium(driver, screenshotName);
    }
}
