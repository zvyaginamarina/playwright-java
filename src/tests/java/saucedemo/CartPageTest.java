package tests.java.saucedemo;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPageTest extends SauceDemoBaseTest {

    ProductsPage productPage;
    CartPage cart;

    @BeforeEach
    void login() {
        LoginPage loginPage = new LoginPage(page);
        String username = "standard_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        productPage = loginPage.loginExpectingSuccess(username, password);
        cart = new CartPage(page);
    }

    @Test
    @DisplayName("Empty cart")
    void emptyCart() {
        productPage.header().openCart();

        assertThat(cart.getCartItem()).hasCount(0);
    }

    @Test
    @DisplayName("One item in cart")
    void oneItemInCart() {
        productPage.addToCart("Sauce Labs Backpack");
        productPage.header().openCart();

        assertThat(cart.getCartItem()).hasCount(1);
    }

    @Test
    @DisplayName("All items in cart")
    void allItemsInCart() {
        String[] products = { "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt",
                "Sauce Labs Fleece Jacket", "Sauce Labs Onesie", "Test.allTheThings() T-Shirt (Red)" };

        for (String product : products) {
            productPage.addToCart(product);
        }

        productPage.header().openCart();

        assertThat(cart.getCartItem()).hasCount(6);
        assertThat(cart.getAllItemName()).hasText(products);
    }

    @Test
    @DisplayName("Remove item and empty cart")
    void removeItemAndEmptyCart() {
        productPage.addToCart("Sauce Labs Backpack");
        productPage.header().openCart();
        cart.removeItem("Sauce Labs Backpack");

        assertThat(cart.itemByName("Sauce Labs Backpack")).not().isVisible();
        assertThat(cart.getCartItem()).hasCount(0);
        assertThat(cart.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Remove item and change item's count")
    void removeOneOfTwoItemsFromCart() {
        productPage.addToCart("Sauce Labs Backpack");
        productPage.addToCart("Sauce Labs Bike Light");
        productPage.header().openCart();
        cart.removeItem("Sauce Labs Backpack");

        assertThat(cart.itemByName("Sauce Labs Backpack")).not().isVisible();
        assertThat(cart.getCartItem()).hasCount(1);
        assertThat(cart.header().getCartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Open product card")
    void openProductCard() {
        productPage.addToCart("Sauce Labs Backpack");
        productPage.header().openCart();
        cart.openItemCard("Sauce Labs Backpack");

        assertThat(page).hasURL(Pattern.compile(".*/inventory-item\\.html"));
    }

}
