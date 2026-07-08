package tests.java;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {
    private Playwright playwright;
    private Browser browser;

    @BeforeAll
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @Test
    void testLoginPage() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        System.out.println("Test start in thread: " + Thread.currentThread().getName() + " at: " + LocalTime.now());

        page.navigate("https://the-internet.herokuapp.com/login");
        assertThat(page).hasTitle("The Internet");
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(2)))
                .containsText("Login Page");

        context.close(); // Закрываем контекст после теста
    }

    @Test
    void testAddRemoveElements() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        System.out.println("Test start in thread: " + Thread.currentThread().getName() + " at: " + LocalTime.now());

        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add Element")).click();
        assertThat(page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).isVisible();

        context.close();
    }

    @AfterAll
    void teardown() {
        browser.close();
        playwright.close();
    }
}