package tests.java.saucedemo;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ItemPageTest extends SauceDemoBaseTest {

    ItemPage itemPage;

    @BeforeEach
    void preconditions() {
        successLogin(User.STANDARD_USER);
    }

    @Test
    @DisplayName("Add to cart")
    void addToCart() {

        itemPage = productsPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Remove");
        assertThat(itemPage.header().getCartBadge()).containsText("1");
    }

    @Test
    @DisplayName("Add to cart one more item")
    void addToCartOneMoreItem() {

        productsPage.addToCart(Products.BACKPACK);
        itemPage = productsPage.openItemPage(Products.BIKELIGHT);
        itemPage.addToCart(page);

        assertThat(itemPage.header().getCartBadge()).containsText("2");
    }

    @Test
    @DisplayName("Remove only item from cart")
    void removeItemFromCart() {

        itemPage = productsPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart(page);
        itemPage.removeFromCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Add to cart");
        assertThat(itemPage.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Remove one of two item from cart")
    void removeOneOfTwoItemFromCart() {

        productsPage.addToCart(Products.BIKELIGHT);
        itemPage = productsPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart(page);
        itemPage.removeFromCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Add to cart");
        assertThat(itemPage.header().getCartBadge()).containsText("1");
    }

}
