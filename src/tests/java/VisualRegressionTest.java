package tests.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class VisualRegressionTest extends BaseSetup {

    @Test
    void testHomePageVisual() throws IOException {
        page.navigate("https://the-internet.herokuapp.com");
        Path expected = Paths.get("target", "screenshots", "expected.png");
        page.screenshot(new ScreenshotOptions().setPath(expected));

        page.reload();
        Path actual = Paths.get("target", "screenshots", "actual.png");
        page.screenshot(new Page.ScreenshotOptions().setPath(actual));

        long mismatch = Files.mismatch(actual, expected);
        assertEquals(-1, mismatch);
    }

}
