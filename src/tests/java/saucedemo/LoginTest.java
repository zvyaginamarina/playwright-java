package tests.java.saucedemo;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends SauceDemoBaseTest {

    @Test
    @DisplayName("Login with standart user")
    void standartUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "standard_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));

    }

    @Test
    @DisplayName("Login with locked_out_user")
    void lockedUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "locked_out_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingFailure(username, password);

        assertThat(loginPage.getLoginError()).containsText("Epic sadface: Sorry, this user has been locked out.");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));

    }

    @Test
    @DisplayName("Login with problem_user")
    void problemUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "problem_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with performance_glitch_user")
    void performanceGlitchUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "performance_glitch_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with error_user")
    void errorUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "error_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with visual_user")
    void visualUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "visual_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, password);

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

}
