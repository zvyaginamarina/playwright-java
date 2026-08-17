package tests.java.saucedemo;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPageTest extends SauceDemoBaseTest {

    ProductsPage productPage;
    CartPage cart;

    @BeforeEach
    void preconditions() {
        successLogin(User.STANDARD_USER);
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
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();

        assertThat(cart.getCartItem()).hasCount(1);
        assertThat(cart.getItemName(Products.BACKPACK)).containsText(Products.BACKPACK);
    }

    @Test
    @DisplayName("All items in cart")
    void allItemsInCart() {
        String[] products = { Products.BACKPACK, Products.BIKELIGHT, Products.TSHIRT, Products.FLEECEJACKET,
                Products.ONESIE, Products.TSHIRT_RED };

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
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        cart.removeItem(Products.BACKPACK);

        assertThat(cart.itemByName(Products.BACKPACK)).not().isVisible();
        assertThat(cart.getCartItem()).hasCount(0);
        assertThat(cart.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Remove item and change item's count")
    void removeOneOfTwoItemsFromCart() {
        productPage.addToCart(Products.BACKPACK);
        productPage.addToCart(Products.BIKELIGHT);
        productPage.header().openCart();
        cart.removeItem(Products.BACKPACK);

        assertThat(cart.itemByName(Products.BACKPACK)).not().isVisible();
        assertThat(cart.getCartItem()).hasCount(1);
        assertThat(cart.header().getCartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Open product card")
    void openProductCard() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        cart.openItemCard(Products.BACKPACK);

        assertThat(page).hasURL(Pattern.compile(".*/inventory-item\\.html"));
    }

}
