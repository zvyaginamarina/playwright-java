package tests.java.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DragDropArea {

    private final Page page;
    private Locator elementA;
    private Locator elementB;

    public DragDropArea(Page page) {
        this.page = page;
        elementA = page.locator("#column-a");
        elementB = page.locator("#column-b");
    }

    public void dragAToB() {
        elementA.dragTo(elementB);
    }

    public Locator getElementB() {
        return page.locator("#column-b");
    }
}
