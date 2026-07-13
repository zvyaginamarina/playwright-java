package tests.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitUntilState;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

public class AdvancedReportingTest extends BaseSetup {

    private static ExtentReports extent;
    private ExtentTest test;
    private String dialogType;
    private String dialogMessage;

    @BeforeAll
    static void setupExtent() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("allure-results/extent-report.html");
        reporter.config().setDocumentTitle("Playwright Extent Report");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeEach
    void initialTest() {
        test = extent.createTest("Test");
    }

    @AfterAll
    static void tearDown() {
        extent.flush();
    }

    @Test
    @Story("Проверка JS Alert")
    @Description("Тест взаимодействия с JS Alert и проверка результата")
    @Severity(SeverityLevel.NORMAL)
    void testJavaScriptAlert() throws Exception {
        try {
            navigateToAlertspage();
            String alertMessage = forJsAlert();
            verifyResultText();
            captureSuccessScreenshot();

            logExtent(Status.PASS, "Тест успешно завершен с сообщением: " + alertMessage);

        } catch (Exception e) {
            forTestFailure(e);
            throw e;
        }
    }

    @Step("Открыть страницу с алертами")
    private void navigateToAlertspage() {
        page().navigate("https://the-internet.herokuapp.com/javascript_alerts",
                new NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        assertEquals("JavaScript Alerts", page().locator("h3").textContent(),
                "Страница должна содержать заголовок 'JavaScript Alerts'");
        logExtent(Status.INFO, "Страница с алертами загружена");
    }

    @Step("Обработать JS Alert")
    private String forJsAlert() {

        page().onDialog(dialog -> {
            dialogType = dialog.type();
            dialogMessage = dialog.message();
            dialog.accept();
        });

        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Click for JS Alert")).click();
        logExtent(Status.INFO, "Клик по кнопке JS Alert выполнен");

        assertEquals("alert", dialogType);
        assertEquals("I am a JS Alert", dialogMessage);

        return dialogMessage;
    }

    @Step("Проверить текст результата")
    private void verifyResultText() {
        page().waitForCondition(() -> page().locator("#result").textContent().contains("successfully"));

        String resultText = page().locator("#result").textContent();
        assertEquals("You successfully clicked an alert", resultText,
                "Текст результата должен соответствовать ожидаемому");
        logExtent(Status.INFO, "Результирующий текст проверен: " + resultText);
    }

    private void captureSuccessScreenshot() throws IOException {
        String screenshotName = "success-screenshot.png";
        Path screenshotPath = Paths.get("allure-results", screenshotName);

        byte[] screenshot = page().screenshot(new ScreenshotOptions().setPath(screenshotPath));

        // Для Allure
        try (InputStream screenshotStream = new ByteArrayInputStream(screenshot)) {
            Allure.addAttachment("Успешное выполнение", "image/png", screenshotStream, ".png");
        }

        // Для ExtentReports

        test.pass("Скриншот успешного выполнения",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath.toString()).build());
    }

    private void logExtent(Status status, String message) {
        test.log(status, message);
    }

    private void forTestFailure(Exception e) {
        // Скриншот для Allure при ошибке
        String screenshotName = "error-screenshot.png";
        Path screenshotPath = Paths.get("allure-results", screenshotName);
        byte[] failureScreenshot = page().screenshot(new ScreenshotOptions().setPath(screenshotPath));

        try (InputStream failureStream = new ByteArrayInputStream(failureScreenshot)) {
            Allure.addAttachment("Ошибка теста", "image/png", failureStream, ".png");
        } catch (Exception ex) {
            logExtent(Status.WARNING, "Не удалось добавить скриншот ошибки в Allure: " + ex.getMessage());
        }

        // Логирование ошибки в ExtentReports

        test.fail("Скриншот ошибки выполнения",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath.toString()).build());
    }

}
