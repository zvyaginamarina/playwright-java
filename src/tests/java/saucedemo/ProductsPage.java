package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductsPage {

    private final Page page;
    private Locator productCard;
    private Locator cart;
    private Locator cartBadge;

    public ProductsPage(Page page) {
        this.page = page;

        productCard = page.getByTestId("inventory-item");
        cart = page.getByTestId("shopping-cart-link");
        cartBadge = page.getByTestId("shopping-cart-badge");
    }

    private void toggleProduct(String productName) {
        getProductCardButton(productName).click();
    }

    public void addToCart(String productName) {
        toggleProduct(productName);
    }

    public void removeFromCart(String productName) {
        toggleProduct(productName);
    }

    public Locator getCartBadge() {
        return cartBadge;
    }

    public Locator getProductCardButton(String productName) {
        return productCard.filter(new FilterOptions().setHasText(productName))
                .getByRole(AriaRole.BUTTON);
    }

    public void openCart() {
        cart.click();
    }

}
