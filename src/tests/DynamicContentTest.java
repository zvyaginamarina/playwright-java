package tests;

import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DynamicContentTest {

    Playwright playwright; // Объект Playwright для взаимодействия с браузером
    Browser browser; // Объект браузера
    BrowserContext context; // Контекст браузера для управления сессией
    Page page; // Страница для взаимодействия с веб-контентом

    @BeforeEach
    void setUp() {
        // Инициализация Playwright и открытие браузера перед каждым тестом
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(true)); // Запуск браузера в режиме с графическим
                                                                            // интерфейсом
        context = browser.newContext(); // Создание нового контекста браузера
        page = context.newPage(); // Открытие новой страницы
    }

    @AfterEach
    void tearDown() {
        // Закрытие Playwright после каждого теста
        playwright.close();
    }

    @Test
    void testDynamicLoading() {
        try {
            // Навигация к странице с динамической загрузкой
            page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");

            // Ожидание видимости кнопки "Start" и клик по ней
            Locator startButton = page.locator("button:has-text('Start')");
            startButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            startButton.click();

            // Ожидание появления текста "Hello World!" после загрузки
            Locator helloWorldText = page.locator("#finish >> text=Hello World!");
            helloWorldText.waitFor(new Locator.WaitForOptions().setTimeout(45000)); // Установка таймаута на 45 секунд

            // Ожидание видимости ссылки "Elemental Selenium" и проверка ее наличия
            Locator seleniumLink = page.locator("text=Elemental Selenium");
            seleniumLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            Assertions.assertTrue(seleniumLink.isVisible(), "Ссылка не отображается");

            // Ожидание новой страницы после клика по ссылке
            Page newPage = context.waitForPage(() -> {
                seleniumLink.click(); // Клик по ссылке
            });

            // Ожидание загрузки новой страницы
            newPage.waitForLoadState(LoadState.LOAD);

            // Проверка URL новой страницы и наличие заголовка
            Assertions.assertTrue(
                    newPage.url().startsWith("https://elementalselenium.com/"), "Фактический URL: " + newPage.url());
            Assertions.assertTrue(newPage.isVisible("h1 >> text='Elemental Selenium'"), "Заголовок не найден");

            // Закрытие новой страницы
            newPage.close();

        } catch (Throwable t) {
            // В случае ошибки сделать скриншот для отладки
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("dynamic-loading-error.png")));
            throw t;
        }
    }
}
