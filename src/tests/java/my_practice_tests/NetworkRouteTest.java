package tests.java.my_practice_tests;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route.FulfillOptions;
import com.microsoft.playwright.Route.ResumeOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;

public class NetworkRouteTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    int responseCode;

    @BeforeAll
    static void playwrightSetUp() {
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
        page.close();
        context.close();
    }

    @AfterAll
    static void playwrightTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void mockResponse() {
        page.route("**/infinite_scroll/1", route -> {
            route.fulfill(new FulfillOptions()
                    .setBody("Hello world! <a href='/infinite_scroll/2'>next page</a>")
                    .setContentType("text/html")
                    .setStatus(200));
        });

        // page.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        page.waitForResponse(Pattern.compile(".*/2"),
                () -> page.navigate("https://the-internet.herokuapp.com/infinite_scroll"));

        assertThat(page.locator(".jscroll-added").first()).containsText("Hello world!");

    }

    @Test
    void abortImgLoad() {
        page.route("**/*", route -> {
            if ("image".equals(route.request().resourceType())) {
                route.abort();
            } else {
                route.resume();
            }
        });

        page.navigate("https://the-internet.herokuapp.com/");

        page.screenshot(new ScreenshotOptions().setPath(Paths.get("target", "downloads", "page-screenshot.jpg")));
    }

    @Test
    void requestMonitoring() {
        page.onRequest(request -> {
            System.out.println("Request method: " + request.method());
            System.out.println("Request URL: " + request.url());
            System.out.println("Request Resource Type: " + request.resourceType());
        });

        page.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        page.waitForLoadState(NETWORKIDLE);
    }

    @Test
    void serverError() {
        page.route("**/infinite_scroll/2", route -> {
            route.fulfill(new FulfillOptions()
                    .setStatus(500)
                    .setBody("Server error"));

        });

        page.onResponse(response -> {
            System.out.println(response.status() + " " + response.url());
            if (response.url().contains("/infinite_scroll/2")) {
                responseCode = response.status();
            }
        });

        page.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        page.waitForLoadState(NETWORKIDLE);
        page.screenshot(new ScreenshotOptions().setPath(Paths.get("target",
                "downloads", "server-error.jpg")));

        assertEquals(500, responseCode);

    }

    @Test
    void unsubscribeToRoute() {
        page.route("**/infinite_scroll/1*", route -> {
            route.fulfill(new FulfillOptions()
                    .setBody("Hello world! <a href='/infinite_scroll/2'>next page</a>"));
        });

        page.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        page.waitForLoadState(NETWORKIDLE);
        assertThat(page.locator(".jscroll-added").first()).containsText("Hello world!");

        page.unroute("**/infinite_scroll/1*");

        page.reload();
        page.waitForLoadState(NETWORKIDLE);

        assertThat(page.locator(".jscroll-added").first()).not().containsText("Hello world!");

        String text = page.locator(".jscroll-added").first().textContent();
        assertTrue(text.length() > 100);

    }

    @Test
    void modifiedHeaders() {
        page.route("**/infinite_scroll/2", route -> {
            Map<String, String> requestHeaders = new HashMap<>(route.request().allHeaders());
            requestHeaders.put("X-Test-Run", "true");
            route.resume(new ResumeOptions().setHeaders(requestHeaders));
        });

        page.onResponse(response -> {
            System.out.println(response.status() + " " + response.url() + " " + response.headers());
        });

        page.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        page.waitForLoadState(NETWORKIDLE);

    }

}
