package tests.java.saucedemo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutPageTest extends SauceDemoBaseTest {

    private CartPage cart;
    private CheckoutInformationPage informationPage;
    private CheckoutOverviewPage overViewPage;
    private CheckoutCompletePage completePage;

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String POSTAL_CODE = "1234";

    @BeforeEach
    void precondition() {
        successLogin(User.STANDARD_USER);
        cart = new CartPage(page);
    }

    void setupConditionOneProduct(String productName) {
        productPage.addToCart(productName);
        productPage.header().openCart();

    }

    void setupConditionTwoProducts(String productNameOne, String productNameTwo) {
        productPage.addToCart(productNameOne);
        productPage.addToCart(productNameTwo);
        productPage.header().openCart();
    }

    @Test
    @DisplayName("Fill information with valid data and continue to overview page")
    void successContinueToOverwievPage() {
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        assertThat(page).hasURL(Pattern.compile(".*/checkout-step-two*"));
    }

    @ParameterizedTest(name = "{3}")
    @CsvSource({
            "'',Doe,1111,Error: First Name is required",
            "John,'',1111,Error: Last Name is required",
            "John,Doe,'',Error: Postal Code is required"
    })
    void oneEmptyFieldInForm(String firstname, String lastname, String postalcode, String message) {
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        informationPage.sendForm(firstname, lastname, postalcode);

        assertThat(informationPage.errorMessage()).containsText(message);
    }

    @Test
    @DisplayName("Return from checkout information page to cart")
    void returnToCart() {
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        informationPage.returnToCart();

        assertThat(page).hasURL(Pattern.compile(".*/cart*"));
    }

    @Test
    @DisplayName("Checkout overview contains all products from cart")
    void itemsOnCheckoutOverviewPage() {
        setupConditionTwoProducts(Products.BACKPACK, Products.BIKELIGHT);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        assertThat(overViewPage.itemName(Products.BACKPACK)).containsText(Products.BACKPACK);
        assertThat(overViewPage.itemName(Products.BIKELIGHT)).containsText(Products.BIKELIGHT);
        assertThat(overViewPage.cartItem()).hasCount(2);
    }

    @Test
    @DisplayName("Checkout overview contains correct payment amount")
    void paymentAmountOncheckoutOverviewPage() {
        setupConditionTwoProducts(Products.BACKPACK, Products.BIKELIGHT);

        BigDecimal backpackPrice = productPage.getItemPrice(Products.BACKPACK);
        BigDecimal bikeLightPrice = productPage.getItemPrice(Products.BIKELIGHT);

        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        BigDecimal subtotal = overViewPage.subtotalAmount();
        BigDecimal tax = overViewPage.taxAmount();
        BigDecimal total = overViewPage.totalAmount();

        BigDecimal expectedSubtotal = backpackPrice.add(bikeLightPrice);
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
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();

        assertThat(page).hasURL(Pattern.compile(".*/checkout-complete*"));
        assertThat(completePage.orderCompleteMessage()).containsText("Thank you for your order!");
    }

    @Test
    @DisplayName("Download PDF order")
    void downloadPDFOrder() throws IOException {
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();
        Path filePath = completePage.downloadPDFOrder();
        File file = filePath.toFile();

        assertTrue(file.length() > 0);
    }

    @Test
    @DisplayName("PDF order contains correct shipment info")
    void correctShipmentInfoInPDFOrder() throws IOException {
        setupConditionOneProduct(Products.BACKPACK);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();
        Path filePath = completePage.downloadPDFOrder();
        String text = PDFExtractor.extractTextFromPdf(filePath);

        assertTrue(text.contains(FIRST_NAME));
        assertTrue(text.contains(LAST_NAME));
        assertTrue(text.contains(POSTAL_CODE));
    }

    @Test
    @DisplayName("PDF order contains correct products and price")
    void correctProductsAndPriceInPDFOrder() throws IOException {
        productPage.addToCart(Products.BACKPACK);
        productPage.addToCart(Products.BIKELIGHT);

        BigDecimal backpackPrice = productPage.getItemPrice(Products.BACKPACK);
        BigDecimal bikelightPrice = productPage.getItemPrice(Products.BIKELIGHT);

        productPage.header().openCart();
        informationPage = cart.openCheckoutInformationPage();

        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();
        Path filePath = completePage.downloadPDFOrder();
        String text = PDFExtractor.extractTextFromPdf(filePath);

        String[] splitedText = text.split("\\n");

        BigDecimal backpackPriceinOrder = PDFExtractor.getValueFromString(splitedText, Products.BACKPACK);
        BigDecimal bikelightPriceinOrder = PDFExtractor.getValueFromString(splitedText, Products.BIKELIGHT);

        assertTrue(text.contains(Products.BACKPACK));
        assertTrue(text.contains(Products.BIKELIGHT));

        assertEquals(0, backpackPriceinOrder.compareTo(backpackPrice),
                "Expect " + backpackPrice + ", but got " + backpackPriceinOrder);
        assertEquals(0, bikelightPriceinOrder.compareTo(bikelightPrice),
                "Expect " + bikelightPrice + ", but got " + bikelightPriceinOrder);

    }

    @Test
    @DisplayName("PDF order contains correct payment amounts")
    void correctPaymentsAmountInPDFOrder() throws IOException {
        setupConditionTwoProducts(Products.BACKPACK, Products.BIKELIGHT);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        BigDecimal expectedSubtotal = overViewPage.subtotalAmount();
        BigDecimal expectedTax = overViewPage.taxAmount();
        BigDecimal expectedTotal = overViewPage.totalAmount();

        completePage = overViewPage.openCheckoutCompletePage();
        Path filePath = completePage.downloadPDFOrder();
        String text = PDFExtractor.extractTextFromPdf(filePath);
        String[] splitedText = text.split("\\n");

        BigDecimal subtotal = PDFExtractor.getValueFromString(splitedText, "Item total");
        BigDecimal tax = PDFExtractor.getValueFromString(splitedText, "Tax");
        BigDecimal total = PDFExtractor.getValueFromString(splitedText, "Total");

        assertEquals(0, subtotal.compareTo(expectedSubtotal), "Expect " + expectedSubtotal + ", but got " + subtotal);
        assertEquals(0, tax.compareTo(expectedTax), "Expect " + expectedTax + ", but got " + tax);
        assertEquals(0, total.compareTo(expectedTotal), "Expect " + expectedTotal + ", but got " + total);
    }

    @Test
    @DisplayName("Cart badge deleted and cart is empty after compliting order")
    void cartBadgeDeletedAndEmptyCartAfterOrderCompete() {
        setupConditionTwoProducts(Products.BACKPACK, Products.BIKELIGHT);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        completePage = overViewPage.openCheckoutCompletePage();

        assertThat(completePage.header().cartBadge()).not().isVisible();

        completePage.header().openCart();
        cart = new CartPage(page);

        assertThat(cart.cartItem()).hasCount(0);
    }

    @Test
    @DisplayName("Checkout E2E test")
    void checkoutProcess() throws IOException {
        productPage.addToCart(Products.BACKPACK);
        productPage.addToCart(Products.BIKELIGHT);

        BigDecimal backpackPriceOnProductsPage = productPage.getItemPrice(Products.BACKPACK);
        BigDecimal bikelightPriceOnProductsPage = productPage.getItemPrice(Products.BIKELIGHT);

        productPage.header().openCart();

        String backpackNameOnCartPage = cart.itemName(Products.BACKPACK).textContent();
        String bikelightNameOnCartPage = cart.itemName(Products.BIKELIGHT).textContent();

        BigDecimal backpackPriceOnCartPage = cart.getCartItemPrice(Products.BACKPACK);
        BigDecimal bikelightPriceOnCartPage = cart.getCartItemPrice(Products.BIKELIGHT);

        assertEquals(Products.BACKPACK, backpackNameOnCartPage);
        assertEquals(Products.BIKELIGHT, bikelightNameOnCartPage);

        assertEquals(0, backpackPriceOnCartPage.compareTo(backpackPriceOnProductsPage),
                "Expect " + backpackPriceOnProductsPage + ", but got " + backpackPriceOnCartPage);
        assertEquals(0, bikelightPriceOnCartPage.compareTo(bikelightPriceOnProductsPage),
                "Expect " + bikelightPriceOnProductsPage + ", but got " + bikelightPriceOnCartPage);

        informationPage = cart.openCheckoutInformationPage();
        overViewPage = informationPage.continueToCheckoutOverviewPage(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        String backpackNameOnOverviewPage = overViewPage.itemName(Products.BACKPACK).textContent();
        String bikelightNameOnOverviewPage = overViewPage.itemName(Products.BIKELIGHT).textContent();

        BigDecimal backpackPriceOnOverviewPage = overViewPage.getItemPrice(Products.BACKPACK);
        BigDecimal bikelightPriceOnOverviewPage = overViewPage.getItemPrice(Products.BIKELIGHT);

        BigDecimal subtotalOnOverviewPage = overViewPage.subtotalAmount();
        BigDecimal taxOnOverviewPage = overViewPage.taxAmount();
        BigDecimal totalOnOverviewPage = overViewPage.totalAmount();

        BigDecimal expectedSubtotal = backpackPriceOnProductsPage.add(bikelightPriceOnProductsPage);
        BigDecimal taxRatio = new BigDecimal("0.08");
        BigDecimal expectedTax = subtotalOnOverviewPage.multiply(taxRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotal = subtotalOnOverviewPage.add(expectedTax);

        assertEquals(Products.BACKPACK, backpackNameOnOverviewPage);
        assertEquals(Products.BIKELIGHT, bikelightNameOnOverviewPage);

        assertEquals(0, backpackPriceOnOverviewPage.compareTo(backpackPriceOnProductsPage),
                "Expect " + backpackPriceOnProductsPage + ", but got " + backpackPriceOnOverviewPage);
        assertEquals(0, bikelightPriceOnOverviewPage.compareTo(bikelightPriceOnProductsPage),
                "Expect " + bikelightPriceOnProductsPage + ", but got " + bikelightPriceOnOverviewPage);

        assertEquals(0, subtotalOnOverviewPage.compareTo(expectedSubtotal),
                "Expect " + expectedSubtotal + ", but got " + subtotalOnOverviewPage);
        assertEquals(0, taxOnOverviewPage.compareTo(expectedTax),
                "Expect " + expectedTax + ", but got " + taxOnOverviewPage);
        assertEquals(0, totalOnOverviewPage.compareTo(expectedTotal),
                "Expect " + expectedTotal + ", but got " + totalOnOverviewPage);

        completePage = overViewPage.openCheckoutCompletePage();

        Path filePath = completePage.downloadPDFOrder();
        String text = PDFExtractor.extractTextFromPdf(filePath);

        String[] splitedText = text.split("\\n");

        BigDecimal backpackPriceinOrder = PDFExtractor.getValueFromString(splitedText, Products.BACKPACK);
        BigDecimal bikelightPriceinOrder = PDFExtractor.getValueFromString(splitedText, Products.BIKELIGHT);
        BigDecimal subtotalInOrder = PDFExtractor.getValueFromString(splitedText, "Item total");
        BigDecimal taxInOrder = PDFExtractor.getValueFromString(splitedText, "Tax");
        BigDecimal totalInOrder = PDFExtractor.getValueFromString(splitedText, "Total");

        assertTrue(text.contains(Products.BACKPACK));
        assertTrue(text.contains(Products.BIKELIGHT));

        assertEquals(0, backpackPriceinOrder.compareTo(backpackPriceOnProductsPage),
                "Expect " + backpackPriceOnProductsPage + ", but got " + backpackPriceinOrder);
        assertEquals(0, bikelightPriceinOrder.compareTo(bikelightPriceOnProductsPage),
                "Expect " + bikelightPriceOnProductsPage + ", but got " + bikelightPriceinOrder);

        assertTrue(text.contains(FIRST_NAME));
        assertTrue(text.contains(LAST_NAME));
        assertTrue(text.contains(POSTAL_CODE));

        assertEquals(0, subtotalInOrder.compareTo(expectedSubtotal),
                "Expect " + expectedSubtotal + ", but got " + subtotalInOrder);
        assertEquals(0, taxInOrder.compareTo(expectedTax), "Expect " + expectedTax + ", but got " + taxInOrder);
        assertEquals(0, totalInOrder.compareTo(expectedTotal), "Expect " + expectedTotal + ", but got " + totalInOrder);
    }

    @Disabled("Known bug: Checkout button is enabled when the cart is empty. Expected: the button is disabled. Actual: the user can complete an order with no items.")
    @Test
    @DisplayName("Checkout with empty cart is unavailable")
    void checkoutWithEmptyCart() {
        productPage.header().openCart();

        assertThat(cart.cartItem()).hasCount(0);
        assertThat(cart.checkoutButton()).not().isEnabled();
    }

}
