package tests.resources;

import java.nio.file.Paths;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.microsoft.playwright.Page.ScreenshotOptions;

import tests.java.my_practice_tests.BaseTest;

public class TestWatcherExtention implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest.page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", context.getDisplayName() + ".png")));

    }
}
