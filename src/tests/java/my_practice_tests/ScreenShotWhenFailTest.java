package tests.java.my_practice_tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

import tests.resources.TestWatcherExtention;

public class ScreenShotWhenFailTest extends BaseTest {

    @Test
    @ExtendWith(TestWatcherExtention.class)
    void screenShotOnFailTest() {
        page.navigate("https://the-internet.herokuapp.com/login");

        page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        page.getByLabel("Password").fill("SuperPassword");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();
        assertThat(page).hasURL(Pattern.compile(".*/secure"));

    }

}
