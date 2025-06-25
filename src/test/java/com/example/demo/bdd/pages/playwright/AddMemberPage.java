package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class AddMemberPage {

    private final Page page;

    // ✅ Updated test IDs based on the new values
    private static final String NAME_INPUT = "[data-testid='add-name-input']";
    private static final String GREETING_INPUT = "[data-testid='add-greeting-input']";
    private static final String SUBMIT_BUTTON = "[data-testid='add-greeting-button']";
    private static final String SUCCESS_MESSAGE = "[data-testid='add-result-message']";
    private static final String ERROR_MESSAGE = "[data-testid='add-error-message']";

    public AddMemberPage(Page page) {
        this.page = page;
    }

    public void enterName(String name) {
        page.locator(NAME_INPUT).fill(name);
    }

    public void enterGreeting(String greeting) {
        page.locator(GREETING_INPUT).fill(greeting);
    }

    public void submit() {
        page.locator(SUBMIT_BUTTON).click();
        page.waitForSelector(SUCCESS_MESSAGE + ", " + ERROR_MESSAGE,
                new Page.WaitForSelectorOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
    }

    public String getSuccessMessage() {
        Locator locator = page.locator(SUCCESS_MESSAGE);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator.innerText();
    }

    public String getErrorMessage() {
        Locator locator = page.locator(ERROR_MESSAGE);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator.innerText();
    }
}
