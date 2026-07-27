package tests.java.saucedemo;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductsPageTest extends SauceDemoBaseTest {

    @Test
    @DisplayName("Adding item to cart")
    void addToCart() {
        login();

        productsPage.addToCart("Sauce Labs Backpack");

        assertThat(productsPage.getProductCardButton("Sauce Labs Backpack")).containsText("Remove");
        assertThat(productsPage.header().getCartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Removing from cart")
    void removeFromCart() {
        login();

        productsPage.addToCart("Sauce Labs Backpack");
        productsPage.removeFromCart("Sauce Labs Backpack");
        assertThat(productsPage.getProductCardButton("Sauce Labs Backpack")).containsText("Add to cart");
        assertThat(productsPage.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Opening cart page")
    void openCartPage() {
        login();

        productsPage.header().openCart();
        assertThat(page).hasURL(Pattern.compile(".*/cart.html"));

    }

}
