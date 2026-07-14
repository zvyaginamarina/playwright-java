package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Touchscreen;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;

public class MobileDynamicControlsTest {
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
                .setUserAgent("Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)")
                .setViewportSize(834, 1194)
                .setDeviceScaleFactor(2)
                .setIsMobile(true)
                .setHasTouch(true);
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @Test
    void testInputEnabling() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");

        Locator enableButton = page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Enable"));
        enableButton.click();

        assertThat(page.getByRole(AriaRole.TEXTBOX)).isEditable();

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

}
