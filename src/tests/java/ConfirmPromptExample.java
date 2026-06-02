package tests.java;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class ConfirmPromptExample {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            // Обработка Confirm
            page.onceDialog(dialog -> {
                if (dialog.type().equals("confirm")) {
                    System.out.println("Confirm dialog detected");
                    dialog.dismiss(); // Нажимаем "Cancel"
                }
            });
            page.locator("button#delete-item").click();
            assert page.locator("#status").textContent().contains("Отменено");

            // Обработка Prompt
            page.onceDialog(dialog -> {
                if (dialog.type().equals("prompt")) {
                    dialog.accept("Playwright"); // Ввод текста + OK
                }
            });
            page.locator("button#ask-name").click();
            assert page.locator("#username").textContent().equals("Playwright");
        }
    }
}
