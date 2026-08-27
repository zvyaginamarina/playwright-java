package tests.java.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import tests.java.saucedemo.components.HeaderComponent;
import tests.java.saucedemo.test_data.InterfaceElements;

public class ItemPage {
    private final Page page;
    private HeaderComponent header;
    private Locator backButton;

    public ItemPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));
        backButton = page.getByTestId("back-to-products");
    }

    public HeaderComponent header() {
        return header;
    }

    public Locator itemButton() {
        return page.locator(".inventory_details_desc_container").getByRole(AriaRole.BUTTON);
    }

    private void toggleButton() {
        itemButton().click();
    }

    public void addToCart() {
        itemButton().waitFor();
        String buttonText = itemButton().textContent();

        if (InterfaceElements.ADD_TO_CART_BUTTON.equals(buttonText)) {
            toggleButton();
        } else {
            throw new IllegalStateException(
                    "Expected '" + InterfaceElements.ADD_TO_CART_BUTTON + "' button, but got " + buttonText);
        }
    }

    public void removeFromCart() {
        itemButton().waitFor();
        String buttonText = itemButton().textContent();

        if (InterfaceElements.REMOVE_FROM_CART_BUTTON.equals(buttonText)) {
            toggleButton();
        } else {
            throw new IllegalStateException(
                    "Expected '" + InterfaceElements.REMOVE_FROM_CART_BUTTON + "' button, but got " + buttonText);
        }
    }

    public ProductsPage backToProductPage() {
        backButton.click();
        return new ProductsPage(page);
    }

}
