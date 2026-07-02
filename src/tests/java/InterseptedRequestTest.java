package tests.java;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Route.ResumeOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

public class InterseptedRequestTest extends BaseSetup {

    @Test
    void hackedRequest() {
        pageForVideo.route("**/authenticate",
                route -> {
                    System.out.println("Запрос перехвачен!");
                    String requestBefore = route.request().postData();
                    System.out.println("Request before: " + requestBefore);
                    String hackedString = requestBefore.replace("tomsmith", "HACKED_USER");
                    route.resume(new ResumeOptions().setPostData(hackedString));
                    String requestAfter = route.request().postData();
                    System.out.println("Request after: " + requestAfter);
                });

        pageForVideo.navigate("https://the-internet.herokuapp.com/login");
        pageForVideo.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Username")).fill("tomsmith");
        pageForVideo.locator("#password").fill("SuperSecretPassword!");
        pageForVideo.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Login")).click();

        assertThat(pageForVideo).not().hasURL(Pattern.compile(".*/secure"));

    }

}
