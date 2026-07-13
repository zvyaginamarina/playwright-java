package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Tracing.StopOptions;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class DynamicLoadingTraceTest extends BaseSetup {
    @Test
    void testDynamicLoadingWithTrace() {

        context().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page().navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Start")).click();

        page().getByText("Hello World!").waitFor();

        assertThat(page().getByText("Hello World!")).isVisible();

        context().tracing().stop(new StopOptions()
                .setPath(Paths.get("target", "traicing", "tracingTest1.zip")));

    }
}
