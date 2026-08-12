package tests.java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

@ExtendWith(CustomReportExtension.class)
public class LoginCustomReportTest extends BaseSetup {

    @Test
    void loginWithReport() {
        LoginPage lp = new LoginPage(page());

        lp.successLogin(page());

        assertThat(page().locator("#flash-messages")).containsText("You logged into a secure area!");
    }

    @AfterAll
    static void generateReport() {
        ThymeleafReportGenerator.generate(
                CustomReportExtension.getResults(),
                "target/custom-report/test-report.html");
    }
}
