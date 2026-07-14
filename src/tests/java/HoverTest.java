package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HoverTest extends BaseSetup {

    @Test
    void testHoverProfiles() {
        page().navigate("https://the-internet.herokuapp.com/hovers");

        Locator userProfile = page().getByAltText("User Avatar");
        Locator userInfo = page().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View profile"));

        int count = userProfile.count();

        for (int i = 0; i < count; i++) {
            userProfile.nth(i).hover();

            assertThat(userInfo).isVisible();

            userInfo.click();

            assertThat(page()).hasURL(Pattern.compile(".*users/" + (i + 1)));

            page().goBack();

        }

    }
}
