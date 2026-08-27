package tests.java.saucedemo.components;

import com.microsoft.playwright.Locator;

public class HeaderComponent {
    private Locator header;

    private Locator cart;
    private Locator cartBadge;

    public HeaderComponent(Locator header) {
        this.header = header;

        cart = header.getByTestId("shopping-cart-link");
        cartBadge = header.getByTestId("shopping-cart-badge");
    }

    public Locator cartBadge() {
        return cartBadge;
    }

    public void openCart() {
        cart.click();
    }

}
