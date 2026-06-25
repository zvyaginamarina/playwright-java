package tests.java.my_practice_tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FixturesTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void browserSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
        System.out.println("BeforeAll");
    }

    @BeforeEach
    void contextSetUp() {
        context = browser.newContext();
        page = context.newPage();
        System.out.println("BeforeEach");
    }

    @AfterEach
    void contextTearDown() {
        context.close();
        System.out.println("AfterEach");
    }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
        System.out.println("AfterAll");
    }

    @Test
    void loginPage1() {
        page.navigate("https://the-internet.herokuapp.com/login");
        assertThat(page).hasURL(Pattern.compile(".*/login"));
    }

    @Test
    void loginPage2() {
        page.navigate("https://the-internet.herokuapp.com/login");
        assertThat(page).hasURL(Pattern.compile(".*/login"));
    }

    @Test
    void loginPage3() {
        page.navigate("https://the-internet.herokuapp.com/login");
        assertThat(page).hasURL(Pattern.compile(".*/login"));
    }

    @Test
    void loginPage4() {
        page.navigate("https://the-internet.herokuapp.com/login");
        assertThat(page).hasURL(Pattern.compile(".*/login"));
    }
}
