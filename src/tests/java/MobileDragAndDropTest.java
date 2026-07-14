package tests.java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.BoundingBox;

public class MobileDragAndDropTest {
    private static boolean HEADLESS = System.getenv("CI") != null;
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    static Browser.NewContextOptions deviceOptions;

    @BeforeAll
    static void browserSetup() {
        playwright = Playwright.create();
        deviceOptions = new Browser.NewContextOptions()
                .setUserAgent(
                        "Mozilla/5.0 (Linux; Android 12; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Mobile Safari/537.36")
                .setViewportSize(384, 873) // Разрешение экрана
                .setDeviceScaleFactor(3.5)
                .setIsMobile(true)
                .setHasTouch(true);
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void testDragAndDropMobile() {
        page.navigate("https://the-internet.herokuapp.com/drag_and_drop");

        Locator blockA = page.locator("#column-a");
        Locator blockB = page.locator("#column-b");

        assertThat(blockA).containsText("A");

        blockA.dragTo(blockB);

        assertThat(blockA).containsText("B");

    }
}
