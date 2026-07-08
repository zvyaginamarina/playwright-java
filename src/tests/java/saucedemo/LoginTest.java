package tests.java.saucedemo;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends SauceDemoBaseTest {

    @Test
    void successLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "standard_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.login(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));

    }

    @Test
    void lockedUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "locked_out_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.login(username, password);

        assertThat(loginPage.getLoginError()).containsText("Epic sadface: Sorry, this user has been locked out.");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));

    }

}

// "problem_user", "performance_glitch_user", "error_user",
// "visual_user"
