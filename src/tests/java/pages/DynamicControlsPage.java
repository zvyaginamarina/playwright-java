package tests.java.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class DynamicControlsPage {

    private final Page page;
    private Locator removeButton;
    private Locator checkBox;

    public DynamicControlsPage(Page page) {
        this.page = page;
        removeButton = page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Remove"));
        checkBox = page.getByRole(AriaRole.CHECKBOX);
    }

    public void openPage() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");
    }

    public void removeCheckBox() {
        removeButton.click();
    }

    public Locator getCheckBox() {
        return checkBox;
    }

}
