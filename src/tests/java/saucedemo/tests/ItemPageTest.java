package tests.java.saucedemo.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tests.java.saucedemo.pages.ItemPage;
import tests.java.saucedemo.test_data.InterfaceElements;
import tests.java.saucedemo.test_data.Products;
import tests.java.saucedemo.test_data.User;

public class ItemPageTest extends SauceDemoBaseTest {

    ItemPage itemPage;

    @BeforeEach
    void preconditions() {
        successLogin(User.STANDARD_USER);
    }

    @Test
    @DisplayName("Add to cart")
    void addToCart() {

        itemPage = productPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart();

        assertThat(itemPage.itemButton()).containsText(InterfaceElements.REMOVE_FROM_CART_BUTTON);
        assertThat(itemPage.header().cartBadge()).containsText("1");
    }

    @Test
    @DisplayName("Add to cart one more item")
    void addToCartOneMoreItem() {

        productPage.addToCart(Products.BACKPACK);
        itemPage = productPage.openItemPage(Products.BIKELIGHT);
        itemPage.addToCart();

        assertThat(itemPage.header().cartBadge()).containsText("2");
    }

    @Test
    @DisplayName("Remove only item from cart")
    void removeItemFromCart() {

        itemPage = productPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart();
        itemPage.removeFromCart();

        assertThat(itemPage.itemButton()).containsText(InterfaceElements.ADD_TO_CART_BUTTON);
        assertThat(itemPage.header().cartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Remove one of two item from cart")
    void removeOneOfTwoItemFromCart() {

        productPage.addToCart(Products.BIKELIGHT);
        itemPage = productPage.openItemPage(Products.BACKPACK);
        itemPage.addToCart();
        itemPage.removeFromCart();

        assertThat(itemPage.itemButton()).containsText(InterfaceElements.ADD_TO_CART_BUTTON);
        assertThat(itemPage.header().cartBadge()).containsText("1");
    }

}
