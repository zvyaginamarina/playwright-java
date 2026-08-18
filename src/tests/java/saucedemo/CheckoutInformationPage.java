package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutInformationPage {
    private final Page page;
    Locator firstName;
    Locator lastName;
    Locator postalCode;
    Locator cancelButton;
    Locator continueButton;

    public CheckoutInformationPage(Page page) {
        this.page = page;
        firstName = page.getByTestId("firstName");
        lastName = page.getByTestId("lastName");
        postalCode = page.getByTestId("postalCode");
        cancelButton = page.getByTestId("cancel");
        continueButton = page.getByTestId("continue");
    }

    public void fillInformationForm(String firstname, String lastname, String postalcode) {
        firstName.fill(firstname);
        lastName.fill(lastname);
        postalCode.fill(postalcode);
        continueButton.click();
    }

    public CheckoutOwerviewPage continueCheckout(String firstname, String lastname, String postalcode) {
        fillInformationForm(firstname, lastname, postalcode);
        return new CheckoutOwerviewPage();
    }

}
