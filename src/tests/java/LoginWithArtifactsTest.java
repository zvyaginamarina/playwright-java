package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;

public class LoginWithArtifactsTest extends BaseSetup {

    @Test
    void testLoginPerformance() {

        long start = System.currentTimeMillis();

        page().navigate("https://the-internet.herokuapp.com/login");
        page().getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        page().locator("#password").fill("SuperSecretPassword!");
        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();

        assertThat(page().locator("#flash-messages")).containsText("You logged into a secure area!");

        long duration = System.currentTimeMillis() - start;

    }

}
