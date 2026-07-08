package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    private Page page;
    private Locator usernameField;
    private Locator passwordField;
    private Locator loginButton;
    private Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;

        usernameField = page.getByTestId("username");
        passwordField = page.getByTestId("password");
        loginButton = page.getByTestId("login-button");
        errorMessage = page.getByRole(AriaRole.HEADING, new GetByRoleOptions().setLevel(3));
    }

    public void openLoginPage() {
        page.navigate("https://www.saucedemo.com/");
    }

    public void login(String username, String password) {
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }

    public Locator getLoginError() {
        return errorMessage;
    }

}
