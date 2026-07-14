package tests.java.saucedemo;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductsPageTest extends SauceDemoBaseTest {

    ProductsPage productPage;

    @BeforeEach
    void login() {
        LoginPage loginPage = new LoginPage(page);
        String username = "standard_user";
        String password = "secret_sauce";

        loginPage.openLoginPage();
        productPage = loginPage.loginExpectingSuccess(username, password);
    }

    @Test
    @DisplayName("Adding item to cart")
    void addToCart() {
        productPage.addToCart("Sauce Labs Backpack");

        assertThat(productPage.getProductCardButton("Sauce Labs Backpack")).containsText("Remove");
        assertThat(productPage.getCartBadge()).containsText("1");

    }

    @Test
    @DisplayName("Removing from cart")
    void removeFromCart() {

        productPage.addToCart("Sauce Labs Backpack");
        productPage.removeFromCart("Sauce Labs Backpack");
        assertThat(productPage.getProductCardButton("Sauce Labs Backpack")).containsText("Add to cart");
        assertThat(productPage.getCartBadge()).not().isVisible();
    }

    @Test
    @DisplayName("Opening cart page")
    void openCartPage() {

        productPage.openCart();
        assertThat(page).hasURL(Pattern.compile(".*/cart.html"));

    }

}
