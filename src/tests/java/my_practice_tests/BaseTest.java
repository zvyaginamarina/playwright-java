package tests.java.my_practice_tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    public static Page page;

    @BeforeAll
    static void browserSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void contextSetUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    // @AfterEach
    // void contextTearDown() {
    // // context.close();
    // }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
    }
}
