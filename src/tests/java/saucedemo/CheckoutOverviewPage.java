package tests.java.saucedemo;

import java.math.BigDecimal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;

public class CheckoutOverviewPage {
    private final Page page;
    private Locator item;
    private Locator subtotal;
    private Locator tax;
    private Locator total;
    private Locator finishButton;
    private Locator cancelButton;

    public CheckoutOverviewPage(Page page) {
        this.page = page;
        item = page.getByTestId("inventory-item");
        subtotal = page.getByTestId("subtotal-label");
        tax = page.getByTestId("tax-label");
        total = page.getByTestId("total-label");
        finishButton = page.getByTestId("finish");
        cancelButton = page.getByTestId("cancel");
    }

    public Locator cartItem() {
        return item;
    }

    public Locator itemByName(String productName) {
        return item.filter(new FilterOptions().setHasText(productName));
    }

    public Locator allItemName() {
        return item.getByTestId("inventory-item-name");
    }

    public Locator itemName(String productName) {
        return itemByName(productName).getByTestId("inventory-item-name");
    }

    public BigDecimal itemPrice(String productName) {
        return new BigDecimal(
                itemByName(productName).getByTestId("inventory-item-price").textContent().replace("$", "").trim());
    }

    public BigDecimal subtotalAmount() {
        return priceParcing(subtotal);
    }

    public BigDecimal taxAmount() {
        return priceParcing(tax);
    }

    public BigDecimal totalAmount() {
        return priceParcing(total);
    }

    public CheckoutCompletePage openCheckoutCompletePage() {
        finishButton.click();
        return new CheckoutCompletePage(page);
    }

    public ProductsPage returtToProductsPage() {
        cancelButton.click();
        return new ProductsPage(page);
    }

    private BigDecimal priceParcing(Locator locator) {
        return new BigDecimal(locator.textContent().replaceAll("[^\\d.]", "").trim());
    }

}
