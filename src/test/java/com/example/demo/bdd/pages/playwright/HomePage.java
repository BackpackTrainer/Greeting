package com.example.demo.bdd.pages.playwright;

import com.microsoft.playwright.Page;

public class HomePage {

    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public String getPageTitle() {
        return page.title();
    }
}
