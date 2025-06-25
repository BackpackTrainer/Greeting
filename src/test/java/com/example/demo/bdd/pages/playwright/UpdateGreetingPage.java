package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class UpdateGreetingPage {

    private final Page page;

    private static final String NAME_INPUT = "[data-testid='update-name-input']";
    private static final String GREETING_INPUT = "[data-testid='update-greeting-input']";
    private static final String UPDATE_BUTTON = "[data-testid='update-greeting-button']";
    private static final String SUCCESS_MESSAGE = "[data-testid='update-result-message']";
    private static final String ERROR_MESSAGE = "[data-testid='update-error-message']";

    public UpdateGreetingPage(Page page) {
        this.page = page;
    }

    public void enterName(String name) {
        page.locator(NAME_INPUT).fill(name);
    }

    public void enterGreeting(String greeting) {
        page.locator(GREETING_INPUT).fill(greeting);
    }

    public void submit() {
        page.locator(UPDATE_BUTTON).click();
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
