package tests.java.saucedemo;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductsPageTest extends SauceDemoBaseTest {

    @BeforeEach
    void preconditions() {
        successLogin(User.STANDARD_USER);
    }

    @Test
    @DisplayName("Adding item to cart")
    void addToCart() {

        productsPage.addToCart(Products.BACKPACK);

        assertThat(productsPage.getProductCardButton(Products.BACKPACK)).containsText("Remove");
        assertThat(productsPage.header().getCartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Removing from cart")
    void removeFromCart() {

        productsPage.addToCart(Products.BACKPACK);
        productsPage.removeFromCart(Products.BACKPACK);
        assertThat(productsPage.getProductCardButton(Products.BACKPACK)).containsText("Add to cart");
        assertThat(productsPage.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Opening cart page")
    void openCartPage() {

        productsPage.header().openCart();
        assertThat(page).hasURL(Pattern.compile(".*/cart.html"));

    }

}
