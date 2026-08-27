package tests.java.saucedemo.tests;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tests.java.saucedemo.test_data.Products;
import tests.java.saucedemo.test_data.User;

public class ProductsPageTest extends SauceDemoBaseTest {

    @BeforeEach
    void preconditions() {
        successLogin(User.STANDARD_USER);
    }

    @Test
    @DisplayName("Adding item to cart")
    void addToCart() {

        productPage.addToCart(Products.BACKPACK);

        assertThat(productPage.productCardButton(Products.BACKPACK)).containsText("Remove");
        assertThat(productPage.header().cartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Removing from cart")
    void removeFromCart() {

        productPage.addToCart(Products.BACKPACK);
        productPage.removeFromCart(Products.BACKPACK);
        assertThat(productPage.productCardButton(Products.BACKPACK)).containsText("Add to cart");
        assertThat(productPage.header().cartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Opening cart page")
    void openCartPage() {

        productPage.header().openCart();
        assertThat(page).hasURL(Pattern.compile(".*/cart.html"));

    }

}
