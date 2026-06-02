package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DomModalExample {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("https://example.com/modal-demo");

            // 1. Открытие модалки
            page.locator("button#open-modal").click();

            // 2. Ожидание появления и работа с элементами
            Locator modal = page.locator(".modal-dialog");
            modal.locator("input#email").fill("test@example.com");
            modal.locator("button#submit").click();

            // 3. Проверка исчезновения модалки
            assertThat(modal).isHidden(); // Автоматическое ожидание скрытия

            // 4. Альтернатива: закрытие через кнопку
            // modal.locator(".close-btn").click();
        }
    }
}
