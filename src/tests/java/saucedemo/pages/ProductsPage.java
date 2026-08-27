package tests.java.saucedemo.pages;

import java.math.BigDecimal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import tests.java.saucedemo.components.HeaderComponent;
import tests.java.saucedemo.test_data.InterfaceElements;

public class ProductsPage {

    private final Page page;
    private HeaderComponent header;
    private Locator productCard;

    public ProductsPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));
        productCard = page.getByTestId("inventory-item");
    }

    public HeaderComponent header() {
        return header;
    };

    private void toggleProduct(String productName) {
        productCardButton(productName).click();
    }

    public void addToCart(String productName) {

        productCardButton(productName).waitFor();
        String buttonText = productCardButton(productName).textContent();

        if (InterfaceElements.ADD_TO_CART_BUTTON.equals(buttonText)) {
            toggleProduct(productName);
        } else {
            throw new IllegalStateException(
                    "Expected '" + InterfaceElements.ADD_TO_CART_BUTTON + "' button for product: " + productName
                            + " but got " + buttonText);
        }
    }

    public void removeFromCart(String productName) {

        productCardButton(productName).waitFor();
        String buttonText = productCardButton(productName).textContent();

        if (InterfaceElements.REMOVE_FROM_CART_BUTTON.equals(buttonText)) {
            toggleProduct(productName);
        } else {
            throw new IllegalStateException(
                    "Expected " + InterfaceElements.REMOVE_FROM_CART_BUTTON + " button for product: " + productName
                            + " but got " + buttonText);
        }
    }

    public Locator productCardButton(String productName) {
        return productCard.filter(new FilterOptions().setHasText(productName))
                .getByRole(AriaRole.BUTTON);
    }

    public Locator sortingDropDown() {
        return page.getByTestId("product-sort-container");
    }

    public void sortByNameAsc() {
        sortingDropDown().selectOption("az");
    }

    public void sortByNameDesc() {
        sortingDropDown().selectOption("za");
    }

    public void sortByPriceAsc() {
        sortingDropDown().selectOption("lohi");
    }

    public void sortByPriceDesc() {
        sortingDropDown().selectOption("hilo");
    }

    public Locator itemByName(String productName) {
        return productCard.filter(new FilterOptions().setHasText(productName))
                .getByTestId("inventory-item-name");
    }

    public ItemPage openItemPage(String productName) {
        itemByName(productName).click();
        return new ItemPage(page);
    }

    public BigDecimal getItemPrice(String productName) {
        String itemPrice = productCard.filter(new FilterOptions().setHasText(productName))
                .getByTestId("inventory-item-price").textContent().replace("$", "").trim();
        return new BigDecimal(itemPrice);
    }
}
