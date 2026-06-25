package tests.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class YandexSearchTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testSearchWithWait() {
        page.navigate("https://ya.ru");

        Locator modal = page.locator(".modal-dialog");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Нет")).click();
        assertThat(modal).isHidden();

        page.getByRole(AriaRole.COMBOBOX).fill("Playwright");
        page.click("button[type='submit']");
        page.waitForSelector("li.serp-item", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

        String firstResult = page.locator("li.serp-item").nth(1).textContent();
        Locator playwrightResults = page.locator("li.serp-item")
                .filter(new Locator.FilterOptions().setHasText("Playwright"));

        Assertions.assertTrue(firstResult.contains("Playwright"));
        Assertions.assertTrue(playwrightResults.count() > 0, "No results with Playwright");
    }

    @Test
    void testNavigationWait() {
        page.navigate("https://ya.ru");

        Locator modal = page.locator(".modal-dialog");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Нет")).click();
        assertThat(modal).isHidden();

        page.getByRole(AriaRole.COMBOBOX).fill("Playwright");
        page.click("button[type='submit']");
        page.waitForURL("**/search**");

        Assertions.assertTrue(page.url().contains("search"));
    }

    @Test
    void testNetworkWait() {
        page.navigate("https://ya.ru");

        Locator modal = page.locator(".modal-dialog");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Нет")).click();
        assertThat(modal).isHidden();

        page.waitForRequest("https://ya.ru/search**", () -> {
            page.getByRole(AriaRole.COMBOBOX).fill("Playwright");
            page.click("button[type='submit']");
        });

        page.waitForSelector("li.serp-item");
    }

    @AfterEach
    void tearDown() {
        playwright.close();
    }
}
