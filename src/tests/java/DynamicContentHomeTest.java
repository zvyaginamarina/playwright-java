package tests.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * @author Oleg Todor
 * @since 2025-03-18
 */
public class DynamicContentHomeTest {

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
    void testFormSubmission() {
        // Навигация к странице входа
        page.navigate("https://the-internet.herokuapp.com/login");

        // Ожидание навигации после заполнения формы и клика по кнопке входа
        page.waitForNavigation(() -> {
            // Заполнение поля имени пользователя
            page.fill("#username", "tomsmith");
            // Заполнение поля пароля
            page.fill("#password", "SuperSecretPassword!");
            // Клик по кнопке отправки формы
            page.click("button[type='submit']");
        });

        // Ожидание появления сообщения об успешном входе
        page.waitForSelector("text=You logged into a secure area!");
    }
}
