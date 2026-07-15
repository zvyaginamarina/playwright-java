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

    public CartPage(Page page) {
        this.page = page;
        header = new HeaderComponent(page.getByTestId("primary-header"));

        cartItem = page.getByTestId("inventory-item");
    }

    public HeaderComponent header() {
        return header;
    };

    public Locator getCartItem() {
        return cartItem;
    }

    public Locator itemByName(String productName) {
        return cartItem.filter(new FilterOptions().setHasText(productName));
    }

    public Locator getAllItemName() {
        return cartItem.getByTestId("inventory-item-name");
    }

    public Locator getItemCardButton(String productName) {
        return itemByName(productName).getByRole(AriaRole.BUTTON,
                new GetByRoleOptions().setName("Remove"));
    }

    public void removeItem(String productName) {
        getItemCardButton(productName).click();
    }

    public Locator getItemName(String productName) {
        return itemByName(productName).getByTestId("inventory-item-name");
    }

    public void openItemCard(String productName) {
        getItemName(productName).click();
    }

}
