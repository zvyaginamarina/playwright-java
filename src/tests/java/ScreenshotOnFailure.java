package tests.java;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import com.microsoft.playwright.Page;

import io.qameta.allure.Allure;

public class ScreenshotOnFailure implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException​(ExtensionContext ctx, Throwable throwable) throws Throwable {

        BaseSetup baseSetup = (BaseSetup) ctx.getRequiredTestInstance();
        Page page = baseSetup.page;

        byte[] screenshot = page.screenshot();
        Allure.addAttachment(
                "Screenshot on Failure",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png");

        throw throwable;
    }
}
