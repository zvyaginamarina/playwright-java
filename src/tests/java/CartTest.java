package tests.java;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

@ExtendWith(ScreenshotOnFailure.class)
public class CartTest extends BaseSetup {

    @Test
    void testCartActions() {
        String screenShotPathName = CreateTimestampFolder.folderNameGenerator();

        BrowserContext contextForVideo = MakeVideo.contextWithVideo(browser());
        Page pageForVideo = contextForVideo.newPage();

        pageForVideo.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        pageForVideo.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add Element")).click();
        MakeScreenshot.screenShot(pageForVideo, screenShotPathName, "after_add.png");

        assertThat(pageForVideo.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).isVisible();

        pageForVideo.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete")).click();
        MakeScreenshot.screenShot(pageForVideo, screenShotPathName, "after_del.png");

        assertThat(pageForVideo.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).not().isVisible();

        contextForVideo.close();

    }

    @Test
    void testCartActionFailure() {
        page().navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add Element")).click();
        assertThat(page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).isVisible();
    }

}
