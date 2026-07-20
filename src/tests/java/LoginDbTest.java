package tests.java;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class LoginDbTest extends BaseSetup {
    private Connection connection;
    private static DbConfig dbConfig;

    @BeforeAll
    static void loadConfig() {
        dbConfig = ConfigFactory.create(DbConfig.class, System.getProperties());
    }

    @BeforeEach
    void setup() throws SQLException {
        // Создание пользователя в БД
        connection = DriverManager.getConnection(
                dbConfig.dbUrl(),
                dbConfig.dbUser(),
                dbConfig.dbPassword());

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "INSERT INTO users (username, password) VALUES ('tomsmith', 'SuperSecretPassword!')");
        }
    }

    @AfterEach
    void teardownDB() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "DELETE FROM users WHERE username = 'tomsmith'");
        }

        if (connection != null)
            connection.close();
    }

    @Test
    void testLoginWithDbUser() throws SQLException {

        String username = null;
        String password = null;

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT username, password FROM users WHERE username = 'tomsmith'")) {

            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
            }
        }

        assertNotNull(username, "Username not found in DB");
        assertNotNull(password, "Password not found in DB");

        page().navigate("https://the-internet.herokuapp.com/login");
        page().getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill(username);
        page().locator("#password").fill(password);
        page().getByRole(AriaRole.BUTTON).click();

        assertThat(page().locator(".flash.success")).isVisible();
        assertThat(page()).hasURL(Pattern.compile(".*/secure"));
    }

}
