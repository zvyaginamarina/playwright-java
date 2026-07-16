package tests.java.pages;

import com.microsoft.playwright.Page;

import tests.java.component.DragDropArea;

public class DragDropPage extends BasePage {

    private DragDropArea dragDropArea;

    public DragDropPage(Page page) {
        super(page);
        dragDropArea = new DragDropArea(page);
    }

    public DragDropArea dragDropArea() {

        return dragDropArea;
    }
}
