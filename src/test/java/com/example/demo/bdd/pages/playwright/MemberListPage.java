package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class MemberListPage {

    private final Page page;

    private static final String GET_ALL_BUTTON = "[data-testid='get-all-members']";
    private static final String CLEAR_BUTTON = "[data-testid='clear-display-button']";
    private static final String MEMBER_ROW = "[data-testid='member-row']";

    public MemberListPage(Page page) {
        this.page = page;
    }

    public void clickGetAllMembers() {
        page.locator(GET_ALL_BUTTON).click();
    }

    public void clickClearAll() {
        page.locator(CLEAR_BUTTON).click();
    }

    // ✅ New method: wait for members to load after Get All
    public void waitForMembersToBeDisplayed() {
        page.locator(MEMBER_ROW)
                .first()
                .waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.VISIBLE));
    }

    // ✅ Use this to get current count, without waiting
    public long getDisplayedMemberCount() {
        return page.locator(MEMBER_ROW).count();
    }

    public String getMemberRowText(int index) {
        Locator row = page.locator(MEMBER_ROW).nth(index);
        row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return row.innerText();
    }
}
