package tests.java.saucedemo;

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

    public Locator getCartBadge() {
        return cartBadge;
    }

    public void openCart() {
        cart.click();
    }

}
