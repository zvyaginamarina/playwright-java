package tests.java.saucedemo;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ItemPageTest extends SauceDemoBaseTest {

    ItemPage itemPage;

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKELIGHT = "Sauce Labs Bike Light";

    @Test
    @DisplayName("Add to cart")
    void addToCart() {
        login();

        itemPage = productsPage.openItemPage(BACKPACK);
        itemPage.addToCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Remove");
        assertThat(itemPage.header().getCartBadge()).containsText("1");
    }

    @Test
    @DisplayName("Add to cart one more item")
    void addToCartOneMoreItem() {
        login();

        productsPage.addToCart(BACKPACK);
        itemPage = productsPage.openItemPage(BIKELIGHT);
        itemPage.addToCart(page);

        assertThat(itemPage.header().getCartBadge()).containsText("2");
    }

    @Test
    @DisplayName("Remove only item from cart")
    void removeItemFromCart() {
        login();

        itemPage = productsPage.openItemPage(BACKPACK);
        itemPage.addToCart(page);
        itemPage.removeFromCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Add to cart");
        assertThat(itemPage.header().getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Remove one of two item from cart")
    void removeOneOfTwoItemFromCart() {
        login();

        productsPage.addToCart(BIKELIGHT);
        itemPage = productsPage.openItemPage(BACKPACK);
        itemPage.addToCart(page);
        itemPage.removeFromCart(page);

        assertThat(itemPage.getItemButton(page)).containsText("Add to cart");
        assertThat(itemPage.header().getCartBadge()).containsText("1");
    }

}
