package tests.java;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

public class CartTest extends BaseSetup {

    @Test
    void testCartActions() {
        String screenShotPathName = CreateTimestampFolder.createFolder();

        context = MakeVideo.contextWithVideo(browser);
        page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add Element")).click();
        MakeScreenshot.screenShot(page, screenShotPathName, "after_add.png");

        assertThat(page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).isVisible();

        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete")).click();
        MakeScreenshot.screenShot(page, screenShotPathName, "after_del.png");

        assertThat(page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).not().isVisible();

    }

}
