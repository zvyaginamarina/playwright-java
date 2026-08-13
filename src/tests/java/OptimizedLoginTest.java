package tests.java;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;

/**
 * Проверяет доступ к защищённой зоне сайта с сессионными куками, полученными
 * заранее.
 * <p>
 * Перед запуском теста происходит: создание отдельных
 * контекста и страницы, выполняется вход, куки сессии сохраняются в переменную.
 * Перед тестом создается новый контекст и страница, полученные куки
 * присваиваются контексту. Вход выполняется один раз на класс, чтобы не платить
 * за него в каждом тесте.
 * <p>
 * Предусловия:
 * <li>
 * логин и пароль валидны
 * </li>
 * <li>
 * сайт доступен
 * </li>
 */

public class OptimizedLoginTest {
    static private Playwright playwright;
    static private Browser browser;
    private BrowserContext context;
    private Page page;
    private static List<Cookie> authCookies;

    private static boolean HEADLESS = System.getenv("CI") != null;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));

        BrowserContext loginContext = browser.newContext();
        Page loginPage = loginContext.newPage();
        authCookies = performLogin(loginPage);

        loginContext.close();

    }

    @BeforeEach
    void setUpContext() {
        context = browser.newContext();

        if (authCookies != null && !authCookies.isEmpty()) {
            context.addCookies(authCookies);
        }

        page = context.newPage();
    }

    @AfterEach
    void tearDownContext() {
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void tearDownBrowser() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }

    private static List<Cookie> performLogin(Page page) {

        LoginPage lp = new LoginPage(page);

        lp.successLogin(page);

        return page.context().cookies();
    }

    @Test
    @DisplayName("Open secure page with session cookie - page should open")
    void testSecureArea() {
        page.navigate("https://the-internet.herokuapp.com/secure");
        assertThat(page.getByRole(AriaRole.HEADING, new GetByRoleOptions().setLevel(2))).containsText("Secure Area");
    }

}
