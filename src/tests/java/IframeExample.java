package tests.java;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class IframeExample {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("https://example.com/nested-frames");

            // Доступ к родительскому фрейму
            Frame parentFrame = page.frame("parent-frame"); // name="parent-frame"

            // Доступ к дочернему фрейму через родительский
            Frame childFrame = parentFrame.childFrames().get(0); // Первый дочерний фрейм

            // Альтернатива: поиск по элементу iframe
            ElementHandle iframeElement = parentFrame.querySelector("iframe.child-class");
            Frame childFrame2 = iframeElement.contentFrame();

            // Взаимодействие с элементом во вложенном фрейме
            childFrame.locator(".nested-button").click();

            // Возврат к основному контексту через page
            page.locator("body").press("Escape"); // Пример действия в основном DOM
        }
    }
}