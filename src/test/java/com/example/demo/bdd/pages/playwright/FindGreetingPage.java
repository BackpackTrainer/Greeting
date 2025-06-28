package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.Page;

public class FindGreetingPage {

    private final Page page;

    private static final String NAME_INPUT = "[data-testid='find-greeting-input']";
    private static final String SUBMIT_BUTTON = "[data-testid='find-greeting-button']";
    private static final String RESULT_MESSAGE = "[data-testid='find-result-message']";
    private static final String ERROR_MESSAGE = "[data-testid='find-error-message']";

    public FindGreetingPage(Page page) {
        this.page = page;
    }

    public void enterName(String name) {
        page.fill(NAME_INPUT, name);
    }

    public void submit() {
        page.click(SUBMIT_BUTTON);
        page.waitForSelector(RESULT_MESSAGE + ", " + ERROR_MESSAGE);
    }

    public String getGreetingMessage() {
        if (page.isVisible(RESULT_MESSAGE)) {
            return page.textContent(RESULT_MESSAGE).trim();
        } else if (page.isVisible(ERROR_MESSAGE)) {
            return page.textContent(ERROR_MESSAGE).trim();
        }
        return "No result or error message found.";
    }
}
