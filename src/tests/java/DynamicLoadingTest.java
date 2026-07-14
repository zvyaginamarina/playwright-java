package tests.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Tracing.StopOptions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED;
import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;

public class DynamicLoadingTest extends BaseSetup {

    int responseCode;

    @Test
    void testDynamicLoading() {
        context().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page().onResponse(response -> {
            if (response.url().contains("/dynamic_loading/")) {
                responseCode = response.status();
            }
        });

        page().navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Start")).click();
        page().waitForLoadState(NETWORKIDLE);

        assertThat(page().getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Hello World!")))
                .isVisible();
        assertEquals(200, responseCode);

        context().tracing().stop(new StopOptions()
                .setPath(Paths.get("target", "traces", "tracingTest1.zip")));
    }

}
