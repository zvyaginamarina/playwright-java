package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class DynamicFormElements extends BaseSetup {

    @Test
    void dynamicElementsTest() {
        page().navigate("https://the-internet.herokuapp.com/dynamic_controls");

        Locator checkBox = page().getByRole(AriaRole.CHECKBOX);
        Locator removeButton = page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Remove"));
        Locator addButton = page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add"));
        Locator message = page().locator("#message");

        assertThat(checkBox).isVisible();

        removeButton.click();
        message.waitFor();

        assertThat(checkBox).not().isVisible();
        assertThat(message).containsText("It's gone!");

        addButton.click();
        message.waitFor();

        assertThat(checkBox).isVisible();

    }

}
