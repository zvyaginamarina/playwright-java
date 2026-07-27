package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import net.datafaker.transformations.Transformer;

public class ItemPage {
    private final Page page;
    private HeaderComponent header;
    private Locator backButton;

    public ItemPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));
        backButton = page.getByTestId("back-to-products");
    }

    public HeaderComponent header() {
        return header;
    }

    public Locator getItemButton(Page page) {
        return page.locator(".inventory_details_desc_container").getByRole(AriaRole.BUTTON);
    }

    public void toggleButton(Page page) {
        getItemButton(page).click();
    }

    public void addToCart(Page page) {
        toggleButton(page);
    }

    public void removeFromCart(Page page) {
        toggleButton(page);
    }

    public ProductsPage backToProducts(Page page) {
        return new ProductsPage(page);
    }

}
