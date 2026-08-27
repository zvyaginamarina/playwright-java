package tests.java.saucedemo.tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import tests.java.saucedemo.config.SaucedemoConfig;
import tests.java.saucedemo.pages.LoginPage;
import tests.java.saucedemo.test_data.User;

public class LoginTest extends SauceDemoBaseTest {

    @Test
    @DisplayName("Login with standart user")
    void standartUserLogin() {
        LoginPage loginPage = new LoginPage(page);

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(User.STANDARD_USER.username(), SaucedemoConfig.INSTANCE.password());

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with standart user, incorrect password")
    void standartUserIncorrectPasswordLogin() {
        LoginPage loginPage = new LoginPage(page);
        String password = "secret_sauce1";

        loginPage.openLoginPage();
        loginPage.login(User.STANDARD_USER.username(), password);

        assertThat(loginPage.loginError())
                .containsText("Epic sadface: Username and password do not match any user in this service");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with standart user, empty password")
    void standartUserEmptyPasswordLogin() {
        LoginPage loginPage = new LoginPage(page);
        String password = "";

        loginPage.openLoginPage();
        loginPage.login(User.STANDARD_USER.username(), password);

        assertThat(loginPage.loginError())
                .containsText("Epic sadface: Password is required");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));
    }

    @Test
    @DisplayName("Login with locked_out_user")
    void lockedUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "locked_out_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        loginPage.login(username, password);

        assertThat(loginPage.loginError()).containsText("Epic sadface: Sorry, this user has been locked out.");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));

    }

    @Test
    @DisplayName("Login with invalid user, correct password")
    void invalidUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "stndrt_user";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, SaucedemoConfig.INSTANCE.password());

        assertThat(loginPage.loginError())
                .containsText("Epic sadface: Username and password do not match any user in this service");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));

    }

    @Test
    @DisplayName("Login with empty user, correct password")
    void emptyUserLogin() {
        LoginPage loginPage = new LoginPage(page);
        String username = "";

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(username, SaucedemoConfig.INSTANCE.password());

        assertThat(loginPage.loginError())
                .containsText("Epic sadface: Username is required");
        assertThat(page).not().hasURL(Pattern.compile(".*/inventory.html"));

    }

    @ParameterizedTest(name = "Login with {0}")
    @EnumSource(value = User.class, names = { "STANDARD_USER", "LOCKED_OUT_USER" }, mode = EnumSource.Mode.EXCLUDE)
    void allUsersLogin(User user) {
        LoginPage loginPage = new LoginPage(page);

        loginPage.openLoginPage();
        loginPage.loginExpectingSuccess(user.username(), SaucedemoConfig.INSTANCE.password());

        assertThat(page).hasURL(Pattern.compile(".*/inventory.html"));
    }

}
