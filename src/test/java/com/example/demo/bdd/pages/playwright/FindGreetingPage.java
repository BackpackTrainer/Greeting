package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class FindGreetingPage {

    private final Page page;

    private static final String NAME_INPUT = "[data-testid='find-greeting-input']";
    private static final String SUBMIT_BUTTON = "[data-testid='find-greeting-button']";
    private static final String RESULT_MESSAGE = "[data-testid='result-message']";
    private static final String ERROR_MESSAGE = "[data-testid='error-message']";

    public FindGreetingPage(Page page) {
        this.page = page;
    }

    public void enterName(String name) {
        page.locator(NAME_INPUT).fill(name);
    }

    public void submit() {
        page.locator(SUBMIT_BUTTON).click();
        page.waitForSelector("[data-testid='result-message'], [data-testid='error-message']",
                new Page.WaitForSelectorOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
    }


    public String getGreetingMessage(String expectedMessage) {
        Locator locator = page.locator("[data-testid='result-message']")
                .filter(new Locator.FilterOptions().setHasText(expectedMessage))
                .filter(new Locator.FilterOptions().setHasNotText("New member"));
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator.innerText();
    }





    public String getErrorMessage() {
        Locator scopedLocator = page.locator("h2:has-text('Find Greeting by Name')").locator(ERROR_MESSAGE);
        scopedLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return scopedLocator.innerText();
    }

}
