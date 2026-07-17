package tests.java.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import tests.java.BaseSetup;
import tests.java.pages.DynamicControlsPage;

public class DynamicControlsTest extends BaseSetup {

    @Test
    void removeCheckBox() {
        DynamicControlsPage controlPage = new DynamicControlsPage(page());
        controlPage.openPage();
        controlPage.removeCheckBox();

        assertThat(controlPage.getCheckBox()).not().isVisible();
    }

}
