package tests.java;

import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginWithArtifactsTest extends BaseSetup {

    @Test
    void testLoginPerformance() {

        LoginPage lp = new LoginPage(page());

        long start = System.currentTimeMillis();

        lp.successLogin(page());

        assertThat(page().locator("#flash-messages")).containsText("You logged into a secure area!");

        long duration = System.currentTimeMillis() - start;

    }

}
