package tests.java.my_practice_tests;

import java.nio.file.Paths;
import java.util.List;
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
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing.StartOptions;
import com.microsoft.playwright.Tracing.StopOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.options.ScreenshotAnimations.DISABLED;
import static com.microsoft.playwright.options.ScreenshotCaret.HIDE;

public class TestRecordingTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void playwrightSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void contextSetUp() {
        context = browser.newContext(new NewContextOptions()
                .setRecordVideoSize(1280, 720)
                .setRecordVideoDir(Paths.get("target/videos/")));
        context.tracing().start(new StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void contextTearDown() {
        context.tracing().stop(new StopOptions()
                .setPath(Paths.get("target", "tracing", "tracingTest1.zip")));
        String videoPath = page.video().path().toString();
        System.out.println("Video path:" + videoPath);
        context.close();
    }

    @AfterAll
    static void playwrightTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void diffPageAreasScrnshots() {
        page.navigate("https://the-internet.herokuapp.com/");

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "full_page.png"))
                .setFullPage(true));

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "viewport.png")));

        Locator gitHubImg = page.getByAltText("Fork me on GitHub");
        gitHubImg.screenshot(new Locator.ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "locator.png")));

    }

    @Test
    void scrnShotsWithOptions() {
        page.navigate("https://the-internet.herokuapp.com/");

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "clip1.png"))
                .setClip(10, 10, 400, 300));

        Locator mainHeader = page.getByRole(AriaRole.HEADING, new GetByRoleOptions().setLevel(1));
        Locator secondHeader = page.getByRole(AriaRole.HEADING, new GetByRoleOptions().setLevel(2));
        Locator gitHubImg = page.getByAltText("Fork me on GitHub");
        Locator link = page.getByRole(AriaRole.LINK, new GetByRoleOptions().setName("A/B Testing"));

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "clip_with_masking.png"))
                .setMask(List.of(mainHeader, secondHeader, gitHubImg, link)));

        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Start")).click();
        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "clip_no_animation.png"))
                .setAnimations(DISABLED));

        page.navigate("https://the-internet.herokuapp.com/login");
        page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).click();
        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", "clip_no_caret.png"))
                .setCaret(HIDE));

    }

    @Test
    void basicTracing() {
        page.navigate("https://the-internet.herokuapp.com/");
        page.getByRole(AriaRole.LINK, new GetByRoleOptions().setName("Form Authentication")).click();
        page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        page.getByLabel("Password").fill("SuperSecretPassword!");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();

    }

    @Test
    void tracingWithGroupping() {
        page.navigate("https://the-internet.herokuapp.com/login");

        context.tracing().group("Login");

        page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        page.getByLabel("Password").fill("SuperSecretPassword!");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();
        assertThat(page).hasURL(Pattern.compile(".*/secure"));

        context.tracing().groupEnd();

        context.tracing().group("Logout");

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Logout")).click();
        assertThat(page).hasURL(Pattern.compile(".*/login"));

        context.tracing().groupEnd();

    }

    @Test
    void videoRecording() {
        page.navigate("https://the-internet.herokuapp.com/");
        page.getByRole(AriaRole.LINK, new GetByRoleOptions().setName("Form Authentication")).click();
        page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        page.getByLabel("Password").fill("SuperSecretPassword!");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();
    }

}
