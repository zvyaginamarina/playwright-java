package tests.java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseSetup {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page pageForVideo;

    protected String runFolder;

    @BeforeAll
    static void playwrightSetup() {
        boolean headLess = System.getenv("CI") != null;

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(headLess));
    }

    @BeforeEach
    void contextSetup() {
        context = browser.newContext();
        pageForVideo = context.newPage();
    }

    @AfterEach
    void contextTearDown() {
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void browserTearDown() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }

}
