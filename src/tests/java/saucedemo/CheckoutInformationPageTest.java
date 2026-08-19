package tests.java.saucedemo;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutInformationPageTest extends SauceDemoBaseTest {

    private CartPage cart;
    private CheckoutInformationPage informationPage;
    private CheckoutOverviewPage overViewPage;
    private CheckoutCompletePage completePage;

    @BeforeEach
    void login() {
        successLogin(User.STANDARD_USER);
        cart = new CartPage(page);
    }

    @Test
    @DisplayName("Fill information with valid data and continue to overview page")
    void successContinueToOverwievPage() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        informationPage.continueToCheckoutOverviewPage("John", "Doe", "1111");

        assertThat(page).hasURL(Pattern.compile(".*/checkout-step-two*"));
    }

    @ParameterizedTest(name = "{4}")
    @CsvSource({
            "'',Doe,1111,Error: First Name is required",
            "John,'',1111,Error: Last Name is required",
            "John,Doe,'',Error: Postal Code is required"
    })
    void oneEmptyFieldInForm(String firstname, String lastname, String postalcode, String message) {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        informationPage.sendForm(firstname, lastname, postalcode);

        assertThat(informationPage.errorMessage()).containsText(message);

    }

    @Test
    @DisplayName("Fill information with valid data and complete order")
    void successContinueToCompletePage() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage("John", "Doe", "1111");
        overViewPage.openCheckoutCompletePage();

        assertThat(page).hasURL(Pattern.compile(".*/checkout-complete*"));
    }

    @Test
    @DisplayName("Download PDF order")
    void downloadPDFOrder() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage("John", "Doe", "1111");
        completePage = overViewPage.openCheckoutCompletePage();
        completePage.downloadPDFOrder();
    }

}
