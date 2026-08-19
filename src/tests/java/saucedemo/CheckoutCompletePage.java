package tests.java.saucedemo;

import java.nio.file.Path;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage {
    private final Page page;
    private Locator backHomeButton;
    private Locator generatePDFOrder;

    CheckoutCompletePage(Page page) {
        this.page = page;
        backHomeButton = page.getByTestId("back-to-products");
        generatePDFOrder = page.getByTestId("generate-pdf-order");
    }

    public void downloadPDFOrder() {
        Download download = page.waitForDownload(
                () -> generatePDFOrder.click());

        download.saveAs(Path.of("target", "files", download.suggestedFilename()));
    }

}
