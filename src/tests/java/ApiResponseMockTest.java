package tests.java;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

public class ApiResponseMockTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    int responseCode;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }

    @Test
    void successMockTest() {
        String replacedResponseBody = "Mocked Success Response";

        page.route("**/status_codes/404", route -> route.fulfill(new Route.FulfillOptions()
                .setStatus(200)
                .setBody(replacedResponseBody)));

        page.navigate("https://the-internet.herokuapp.com/status_codes");

        page.onResponse(response -> {
            if (response.url().contains("/status_codes/404")) {
                responseCode = response.status();
            }
        });

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("404")).click();

        Locator result = page.locator("html");
        assertEquals(200, responseCode);
        assertThat(result).containsText(replacedResponseBody);

    }

}
