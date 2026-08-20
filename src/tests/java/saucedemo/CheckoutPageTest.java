package tests.java.saucedemo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutPageTest extends SauceDemoBaseTest {

    private CartPage cart;
    private CheckoutInformationPage informationPage;
    private CheckoutOverviewPage overViewPage;
    private CheckoutCompletePage completePage;

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String POSTAL_CODE = "1234";

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

        informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

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
    @DisplayName("Return from checkout information page to cart")
    void returnToCart() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        informationPage.returnToCart();

        assertThat(page).hasURL(Pattern.compile(".*/cart*"));
    }

    @Test
    @DisplayName("Checkout overview contains all products from cart")
    void itemsOnCheckoutOverviewPage() {
        productPage.addToCart(Products.BACKPACK);
        productPage.addToCart(Products.BIKELIGHT);
        productPage.header().openCart();

        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        assertThat(overViewPage.itemName(Products.BACKPACK)).containsText(Products.BACKPACK);
        assertThat(overViewPage.itemName(Products.BIKELIGHT)).containsText(Products.BIKELIGHT);
        assertThat(overViewPage.cartItem()).hasCount(2);
    }

    @Test
    @DisplayName("Checkout overview contains correct payment amount")
    void paymentAmountOncheckoutOverviewPage() {
        productPage.addToCart(Products.BACKPACK);
        productPage.addToCart(Products.BIKELIGHT);

        productPage.header().openCart();

        BigDecimal backpackItemPrice = productPage.getItemPrice(Products.BACKPACK);
        BigDecimal bikeLightItemPrice = productPage.getItemPrice(Products.BIKELIGHT);

        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        BigDecimal subtotal = overViewPage.subtotalAmount();
        BigDecimal tax = overViewPage.taxAmount();
        BigDecimal total = overViewPage.totalAmount();

        BigDecimal expectedSubtotal = backpackItemPrice.add(bikeLightItemPrice);
        BigDecimal taxRatio = new BigDecimal("0.08");
        BigDecimal expectedTax = subtotal.multiply(taxRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotal = subtotal.add(expectedTax);

        assertEquals(0, subtotal.compareTo(expectedSubtotal), "Expect " + expectedSubtotal + ", but got " + subtotal);
        assertEquals(0, tax.compareTo(expectedTax), "Expect " + expectedTax + ", but got " + tax);
        assertEquals(0, total.compareTo(expectedTotal), "Expect " + expectedTotal + ", but got " + total);
    }

    @Test
    @DisplayName("Fill information with valid data and complete order")
    void successContinueToCompletePage() {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();

        assertThat(page).hasURL(Pattern.compile(".*/checkout-complete*"));
        assertThat(completePage.orderCompleteMessage()).containsText("Thank you for your order!");
    }

    @Test
    @DisplayName("Download PDF order")
    void downloadPDFOrder() throws IOException {
        productPage.addToCart(Products.BACKPACK);
        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();
        String fileName = completePage.downloadPDFOrder();
        String text = PDFExtractor.extractTextFromPdf(fileName);

        assertTrue(text.contains(FIRST_NAME));
        assertTrue(text.contains(LAST_NAME));
        assertTrue(text.contains(POSTAL_CODE));
        assertTrue(text.contains(Products.BACKPACK));
    }

}
