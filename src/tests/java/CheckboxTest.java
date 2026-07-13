package tests.java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

@Epic("Веб-интерфейс тестов")
@Feature("Операции с чекбоксами")
public class CheckboxTest {
    static private Playwright playwright;
    static private Browser browser;
    private BrowserContext context;
    private Page page;

    private Locator checkBox1;
    private Locator checkBox2;

    private static boolean HEADLESS = System.getenv("CI") != null;

    @BeforeAll
    @Step("Инициализация браузера")
    static void browserSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
    }

    @BeforeEach
    @Step("Инициализация контекста")
    void contextSetUp() {
        context = browser.newContext();
        page = context.newPage();

        checkBox1 = page.getByRole(AriaRole.CHECKBOX).nth(0);
        checkBox2 = page.getByRole(AriaRole.CHECKBOX).nth(1);
    }

    @Test
    @Story("Проверка работы чекбоксов")
    @DisplayName("Тестирование выбора/снятия чекбоксов")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        navigateToCheckboxesPage();
        verifyInitialState();
        toggleCheckboxes();
        verifyToggledState();
    }

    @Step("Переход на страницу /checkboxes")
    private void navigateToCheckboxesPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
    }

    @Step("Проверка начального состояния чекбоксов")
    private void verifyInitialState() {
        assertThat(checkBox1).not().isChecked();
        assertThat(checkBox2).isChecked();
    }

    @Step("Изменение состояния чекбоксов")
    private void toggleCheckboxes() {
        checkBox1.setChecked(true);
        checkBox2.setChecked(false);
    }

    @Step("Проверка состояния чекбоксов после изменения")
    private void verifyToggledState() {
        assertThat(checkBox1).isChecked();
        assertThat(checkBox2).not().isChecked();
    }

    @AfterEach
    @Step("Закрытие контекста")
    void contextTearDown() {
        context.close();
    }

    @AfterAll
    @Step("Закрытие ресурсов")
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}
