package tests.java.saucedemo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Locator.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {
    private final Page page;
    private HeaderComponent header;
    private Locator cartItem;
    private Locator checkoutButton;
    private Locator continueShoppingButton;

    public CartPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));

        cartItem = page.getByTestId("inventory-item");
        checkoutButton = page.getByTestId("checkout");
        continueShoppingButton = page.getByTestId("continue-shopping");
    }

    public HeaderComponent header() {
        return header;
    };

    public Locator cartItem() {
        return cartItem;
    }

    public Locator itemByName(String productName) {
        return cartItem.filter(new FilterOptions().setHasText(productName));
    }

    public Locator allItemName() {
        return cartItem.getByTestId("inventory-item-name");
    }

    public Locator itemCardButton(String productName) {
        return itemByName(productName).getByRole(AriaRole.BUTTON,
                new GetByRoleOptions().setName("Remove"));
    }

    public void removeItem(String productName) {
        itemCardButton(productName).click();
    }

    public Locator itemName(String productName) {
        return itemByName(productName).getByTestId("inventory-item-name");
    }

    public ItemPage openItemPage(String productName) {
        itemName(productName).click();
        return new ItemPage(page);
    }

    public CheckoutInformationPage openCheckoutInformationPage() {
        checkoutButton.click();
        return new CheckoutInformationPage(page);
    }

    public ProductsPage openProductsPage() {
        continueShoppingButton.click();
        return new ProductsPage(page);
    }

}
