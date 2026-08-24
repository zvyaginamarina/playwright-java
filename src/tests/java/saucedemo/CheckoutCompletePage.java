package tests.java.saucedemo;

import java.nio.file.Path;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage {
    private final Page page;
    private Locator backHomeButton;
    private Locator generatePDFOrder;
    private Locator orderCompleteMessage;

    CheckoutCompletePage(Page page) {
        this.page = page;
        backHomeButton = page.getByTestId("back-to-products");
        generatePDFOrder = page.getByTestId("generate-pdf-order");
        orderCompleteMessage = page.getByTestId("complete-header");
    }

    public Path downloadPDFOrder() {
        Download download = page.waitForDownload(
                () -> generatePDFOrder.click());

        String fileName = download.suggestedFilename();
        Path filePath = Path.of("target", "files", fileName);
        download.saveAs(filePath);

        return filePath;
    }

    public Locator orderCompleteMessage() {
        return orderCompleteMessage;
    }

}
