package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductsPage {

    private final Page page;
    private HeaderComponent header;
    private Locator productCard;

    public ProductsPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));
        productCard = page.getByTestId("inventory-item");
    }

    public HeaderComponent header() {
        return header;
    };

    private void toggleProduct(String productName) {
        getProductCardButton(productName).click();
    }

    public void addToCart(String productName) {
        toggleProduct(productName);
    }

    public void removeFromCart(String productName) {
        toggleProduct(productName);
    }

    public Locator getProductCardButton(String productName) {
        return productCard.filter(new FilterOptions().setHasText(productName))
                .getByRole(AriaRole.BUTTON);
    }

}
