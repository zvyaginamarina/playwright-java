package tests.java.my_practice_tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Touchscreen;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;

public class TouchScreenTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    BrowserContext mobileContext;
    Page page;
    Page mobilePage;

    @BeforeAll
    static void playwrightSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void contextSetUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void contextTearDown() {
        if (mobileContext != null) {
            mobileContext.close();
        }
        context.close();
    }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void touchScreenTapLink() {
        mobileContext = browser.newContext(new NewContextOptions()
                .setHasTouch(true)
                .setIsMobile(true)
                .setViewportSize(430, 932)
                .setUserAgent(
                        "Mozilla/5.0 (iPhone; CPU iPhone OS 18_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/26.0 Mobile/15E148 Safari/604.1"));

        mobilePage = mobileContext.newPage();

        mobilePage.navigate("https://wikipedia.org");

        Locator pageLink = mobilePage.getByTitle("English — Wikipedia — The Free Encyclopedia");
        BoundingBox linkBox = pageLink.boundingBox();
        double linkX = linkBox.x;
        double linkY = linkBox.y;
        double lWidth = linkBox.width;
        double lHeight = linkBox.height;
        double lWCenter = linkX + lWidth / 2;
        double lHCenter = linkY + lHeight / 2;

        Touchscreen touchscreen = mobilePage.touchscreen();
        touchscreen.tap(lWCenter, lHCenter);

        assertThat(mobilePage).hasURL(Pattern.compile(".*/Main_Page"));

    }

    @Test
    void contextMenuHiddenDesktop() {
        page.navigate("https://www.w3schools.com/");

        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Menu"))).isHidden();

    }

    @Test
    void contextMenuVisibleMobile() {
        mobileContext = browser.newContext(new NewContextOptions()
                .setHasTouch(true)
                .setIsMobile(true)
                .setViewportSize(430, 932)
                .setUserAgent(
                        "Mozilla/5.0 (iPhone; CPU iPhone OS 18_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/26.0 Mobile/15E148 Safari/604.1"));

        mobilePage = mobileContext.newPage();

        mobilePage.navigate("https://www.w3schools.com/");

        Locator menuButton = mobilePage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Menu"));

        assertThat(menuButton).isVisible();

        BoundingBox menuButtonBox = menuButton.boundingBox();
        double mWCenter = menuButtonBox.x + menuButtonBox.width / 2;
        double mHCenter = menuButtonBox.y + menuButtonBox.height / 2;

        Touchscreen touch = mobilePage.touchscreen();
        touch.tap(mWCenter, mHCenter);

        assertThat(mobilePage.getByRole(AriaRole.NAVIGATION)).isVisible();

    }

}
