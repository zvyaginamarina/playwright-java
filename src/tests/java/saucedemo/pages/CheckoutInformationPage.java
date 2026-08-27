package tests.java.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutInformationPage {
    private final Page page;
    private final Locator firstName;
    private final Locator lastName;
    private final Locator postalCode;
    private final Locator cancelButton;
    private final Locator continueButton;
    private final Locator error;

    public CheckoutInformationPage(Page page) {
        this.page = page;
        firstName = page.getByTestId("firstName");
        lastName = page.getByTestId("lastName");
        postalCode = page.getByTestId("postalCode");
        cancelButton = page.getByTestId("cancel");
        continueButton = page.getByTestId("continue");
        error = page.getByTestId("error");
    }

    public void sendForm(String firstname, String lastname, String postalcode) {
        firstName.fill(firstname);
        lastName.fill(lastname);
        postalCode.fill(postalcode);
        continueButton.click();
    }

    public CheckoutOverviewPage continueToCheckoutOverviewPage(String firstname, String lastname, String postalcode) {
        sendForm(firstname, lastname, postalcode);
        return new CheckoutOverviewPage(page);
    }

    public Locator errorMessage() {
        return error;
    }

    public CartPage returnToCart() {
        cancelButton.click();
        return new CartPage(page);
    }

}
