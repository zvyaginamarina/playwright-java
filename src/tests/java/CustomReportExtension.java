package tests.java;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

import com.microsoft.playwright.Page;

public class CustomReportExtension
        implements TestWatcher, BeforeTestExecutionCallback, TestExecutionExceptionHandler, AfterTestExecutionCallback {

    private static final List<TestResult> results = new ArrayList<>();
    private long startTime;
    private long duration;
    private String screenshotPath;

    @Override
    public void handleTestExecutionException(
            ExtensionContext context,
            Throwable cause) throws Throwable {

        Page currentPage = ((BaseSetup) context.getRequiredTestInstance()).page();

        Path screenshotFile = Paths.get(
                "target",
                "custom-report",
                "screenshots",
                context.getDisplayName() + ".png");

        Files.createDirectories(screenshotFile.getParent());

        screenshotPath = "screenshots/" + context.getDisplayName() + ".png";
        currentPage
                .screenshot(new Page.ScreenshotOptions().setPath(Paths.get("target", "custom-report", screenshotPath)));

        throw cause;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        duration = System.currentTimeMillis() - startTime;
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        results.add(new TestResult(
                context.getDisplayName(),
                "Passed",
                duration,
                null,
                null));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        results.add(new TestResult(
                context.getDisplayName(),
                "Failed",
                duration,
                cause.getMessage(),
                screenshotPath));
    }

    public static List<TestResult> getResults() {
        return results;
    }
}

class TestResult {
    String name;

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public long getDuration() {
        return duration;
    }

    public String getError() {
        return error;
    }

    public String getScreenshot() {
        return screenshot;
    }

    String status;
    long duration;
    String error;
    String screenshot;

    public TestResult(String name, String status, long duration, String error, String screenshot) {
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.error = error;
        this.screenshot = screenshot;
    }
}