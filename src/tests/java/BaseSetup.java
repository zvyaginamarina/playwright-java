package tests.java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseSetup {

    private static boolean HEADLESS = System.getenv("CI") != null;

    private static final ThreadLocal<Playwright> playwrightHolder = ThreadLocal.withInitial(() -> Playwright.create());

    private static final ThreadLocal<Browser> browserHolder = ThreadLocal
            .withInitial(() -> playwrightHolder.get().chromium().launch(new LaunchOptions().setHeadless(HEADLESS)));

    private static final ThreadLocal<BrowserContext> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageHolder = new ThreadLocal<>();

    protected Browser browser() {
        return browserHolder.get();
    }

    protected BrowserContext context() {
        return contextHolder.get();
    }

    protected Page page() {
        return pageHolder.get();
    }

    protected String runFolder;

    @BeforeEach
    void contextSetup() {
        contextHolder.set(browser().newContext());
        pageHolder.set(context().newPage());
    }

    @AfterEach
    void contextTearDown() {
        if (pageHolder.get() != null) {
            pageHolder.get().close();
            pageHolder.remove();
        }
        if (contextHolder.get() != null) {
            contextHolder.get().close();
            contextHolder.remove();
        }
    }

    @AfterAll
    static void teardownThread() {
        // per-thread ресурсы гасим и ОБЯЗАТЕЛЬНО чистим холдер
        if (browserHolder.get() != null) {
            browserHolder.get().close();
            browserHolder.remove();
        }
        if (playwrightHolder.get() != null) {
            playwrightHolder.get().close();
            playwrightHolder.remove();
        }
    }
}
