package tests.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ModernAssertionsTest {

    @Test
    void testWithModernAssertions() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("http://google.com");

            // 1. Проверка видимости элемента
            assertThat(page.locator("#submit-button")).isVisible();

            // 2. Проверка текста элемента
            assertThat(page.locator("h1.title")).hasText("Добро пожаловать");

            // 3. Проверка атрибута
            assertThat(page.locator("a#link")).hasAttribute("href", "/home");

            // 4. Проверка количества элементов
            assertThat(page.locator(".item")).hasCount(3);

            // 5. Проверка отсутствия элемента
            assertThat(page.locator("#deleted-element")).isHidden(); // Или .not().isVisible()

            // 6. Проверка значения инпута
            assertThat(page.locator("input#email")).hasValue("user@example.com");

            // 7. Проверка заголовка страницы
            assertThat(page).hasTitle("Главная страница");

            // 8. Проверка URL
            assertThat(page).hasURL("https://example.com/home");
        }
    }

    @Test
    void testWithClassicAssertions() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("http://example.com");

            // 1. Проверка видимости элемента (JUnit)
            assertTrue(page.locator("#submit-button").isVisible(),
                    "Кнопка должна быть видимой");

            // 2. Проверка текста элемента (JUnit)
            String header = page.locator("h1.title").textContent();
            assertEquals("Добро пожаловать", header);

            // 3. Проверка количества элементов (JUnit)
            int count = page.locator(".item").count();
            assertEquals(3, count);
        }
    }
}