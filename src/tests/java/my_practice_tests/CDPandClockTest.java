package tests.java.my_practice_tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.CDPSession;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

public class CDPandClockTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

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

    @AfterEach
    void contextTearDown() {
        context.close();
    }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void clockOnTimer() {
        long testStartTime = System.currentTimeMillis();
        page.clock().install();

        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove")).click();
        page.clock().runFor(30000);

        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add"))).isVisible();

        long testEndTime = System.currentTimeMillis();
        long testRunTime = testEndTime - testStartTime;
        System.out.println("Test run time: " + testRunTime);

        assertTrue(testRunTime < 5000);

    }

    @Test
    void slowNetworkEmulation() {
        long testStartTime = System.currentTimeMillis();

        CDPSession client = context.newCDPSession(page);
        JsonObject networkParams = new JsonObject();
        networkParams.addProperty("offline", false);
        networkParams.addProperty("latency", 2000);
        networkParams.addProperty("downloadThroughput", 50000);
        networkParams.addProperty("uploadThroughput", 20000);

        client.send("Network.emulateNetworkConditions", networkParams);

        page.navigate("https://the-internet.herokuapp.com/dynamic_content");

        long testEndTime = System.currentTimeMillis();

        System.out.println("Test run time: " + (testEndTime - testStartTime));

        assertThat(page).hasURL("https://the-internet.herokuapp.com/dynamic_content");

        client.detach();
    }

    @Test
    void normalNetwork() {
        long testStartTime = System.currentTimeMillis();

        page.navigate("https://the-internet.herokuapp.com/dynamic_content");

        long testEndTime = System.currentTimeMillis();

        System.out.println("Test run time: " + (testEndTime - testStartTime));

        assertThat(page).hasURL("https://the-internet.herokuapp.com/dynamic_content");

    }

}
