package tests.java;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    private Page page;
    private Locator usernameField;
    private Locator passwordField;
    private Locator loginButton;
    private static String USERNAME = "tomsmith";
    private static String PASSWORD = "SuperSecretPassword!";

    public LoginPage(Page page) {
        this.page = page;
        usernameField = page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username"));
        passwordField = page.locator("#password");
        loginButton = page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login"));
    }

    public void successLogin(Page page) {
        page.navigate("https://the-internet.herokuapp.com/login");
        usernameField.fill(USERNAME);
        passwordField.fill(PASSWORD);
        loginButton.click();
    }

}
