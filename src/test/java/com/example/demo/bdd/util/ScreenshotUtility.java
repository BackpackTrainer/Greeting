package com.example.demo.bdd.util;

import com.microsoft.playwright.Page;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtility {

    private static final String SCREENSHOTS_DIR = "screenshots";

    // Ensure the screenshots directory exists
    private static void ensureDirectoryExists() throws IOException {
        Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
    }

    // Generate timestamped filename
    private static String generateTimestampedFilename(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseName + "_" + timestamp + ".png";
    }

    // Selenium version
    public static void takeScreenshotWithSelenium(WebDriver driver, String baseName) throws IOException {
        ensureDirectoryExists();
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String fullPath = SCREENSHOTS_DIR + "/" + generateTimestampedFilename(baseName);
        File destFile = new File(fullPath);
        Files.copy(scrFile.toPath(), destFile.toPath());
        System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
    }

    // Playwright version
    public static void takeScreenshotWithPlaywright(Page page, String baseName) throws IOException {
        ensureDirectoryExists();
        String fullPath = SCREENSHOTS_DIR + "/" + generateTimestampedFilename(baseName);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(fullPath)));
        System.out.println("Screenshot saved: " + Paths.get(fullPath).toAbsolutePath());
    }
}
