package tests.java.my_practice_tests;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.WebError;
import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;
import com.microsoft.playwright.options.ServiceWorkerPolicy;

public class ErrorAndExceptionsTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    List<WebError> errors = new ArrayList<>();
    List<ConsoleMessage> messages = new ArrayList<>();

    @BeforeAll
    static void playwrightSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
    }

    // сброс кеша для контекста не решил проблему падения тестов при обзем запуске,
    // попробовать создавать изолированный браузер для каждого теста
    @BeforeEach
    void contextSetUp() {
        context = browser.newContext(new NewContextOptions()
                .setBypassCSP(true)
                .setServiceWorkers(ServiceWorkerPolicy.BLOCK));
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
    void expectedTimeOutError() {
        page.navigate("https://the-internet.herokuapp.com/dropdown");

        page.locator("#dropdown").click();

        TimeoutError exception = assertThrows(TimeoutError.class,
                () -> page.locator("#dropdown > option:nth-child(2)")
                        .click(new Locator.ClickOptions().setTimeout(5000)));

        assertTrue(exception.getMessage().contains("Timeout"));

    }

    @Test
    void expectedPlaywrightError() {
        page.navigate("https://the-internet.herokuapp.com/dropdown");

        PlaywrightException exception = assertThrows(PlaywrightException.class,
                () -> page.locator(":::invalid:::").click());

        if (exception instanceof TimeoutError) {
            System.out.println("This is a timeout");
        } else {
            System.out.println("This is a non-timeout PlaywrightException");
        }

        // invalid CSS selector → PlaywrightException (not TimeoutError)
        assertNotEquals(TimeoutError.class, exception.getClass());

    }

    @Test
    void screenshotAfterThrownException() {
        page.navigate("https://the-internet.herokuapp.com/challenging_dom");

        try {
            page.locator(".large-2 columns:nth-child(0)").click();

        } catch (PlaywrightException e) {
            page.screenshot(new ScreenshotOptions()
                    .setPath(Paths.get("target", "screenshots", "locator-not-found.png")));
            throw e;
        }
    }

    @Test
    void catchJSError() {
        context.onWebError(error -> {
            errors.add(error);
        });

        page.navigate("https://the-internet.herokuapp.com/javascript_error");
        page.waitForLoadState(NETWORKIDLE);

        for (WebError error : errors) {
            System.out.println("JS error: " + error.error());
        }

        assertTrue(errors.size() > 0);
        assertTrue(!errors.isEmpty());

    }

    @Test
    void catchConsoleLogs() {
        page.onConsoleMessage(message -> messages.add(message));

        page.navigate("https://the-internet.herokuapp.com/javascript_error");
        page.waitForLoadState(NETWORKIDLE);

        List<ConsoleMessage> errorMessages = messages.stream().filter(message -> "error".equals(message.type()))
                .collect(toList());

        for (ConsoleMessage message : errorMessages) {
            System.out.println(message.type() + " " + message.text());
        }

        // assertFalse на errorMessages может падать в batch из-за HTTP cache; на
        // изолированном запуске проходит
        assertTrue(!messages.isEmpty());
        assertTrue(!errorMessages.isEmpty());
    }

    @Test
    void catchAllTypeOfErrors() {
        context.onWebError(error -> errors.add(error));
        page.onConsoleMessage(message -> messages.add(message));

        page.navigate("https://the-internet.herokuapp.com/javascript_error");
        page.waitForLoadState(NETWORKIDLE);

        List<ConsoleMessage> errorMessages = messages.stream().filter(message -> "error".equals(message.type()))
                .collect(toList());

        for (WebError error : errors) {
            System.out.println("JS ERROR: " + error.error());
        }

        System.out.println("");

        for (ConsoleMessage message : errorMessages) {
            System.out.println(message.type().toUpperCase() + ": " + message.text());
        }

        // assertFalse на errorMessages может падать в batch из-за HTTP cache; на
        // изолированном запуске проходит
        assertFalse(errors.isEmpty());
        assertFalse(messages.isEmpty());
        assertFalse(errorMessages.isEmpty());
    }

}
