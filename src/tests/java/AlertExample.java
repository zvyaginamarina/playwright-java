package tests.java;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class AlertExample {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            // Слушатель для автоматического принятия Alert
            page.onDialog(dialog -> {
                System.out.println("Alert text: " + dialog.message());
                dialog.accept(); // Обязательно закрыть диалог
            });

            page.navigate("https://example.com/alert-demo");
            page.locator("button#show-alert").click(); // Триггер Alert

            // Проверка, что после Alert произошел переход
            assert page.url().contains("success");
        }
    }
}