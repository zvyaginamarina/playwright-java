package tests.java.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import tests.java.BaseSetup;
import tests.java.pages.DragDropPage;

public class DragDropTest extends BaseSetup {

    @Test
    public void testDragAndDrop() {
        DragDropPage dragDropPage = new DragDropPage(page());
        dragDropPage.navigateTo("https://the-internet.herokuapp.com/drag_and_drop");
        dragDropPage.dragDropArea().dragAToB();
        assertThat(dragDropPage.dragDropArea().getElementB()).containsText("A");
    }
}
