package tests.java;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;

import config.EnvironmentConfig;

public class StatusCodeApiUiTest extends BaseSetup {

    APIRequestContext request;
    private EnvironmentConfig config;

    @BeforeEach
    void apiContextSetup() {
        config = ConfigFactory.create(EnvironmentConfig.class, System.getenv());
        APIRequest apiRequest = playwright().request();
        request = apiRequest.newContext(new APIRequest.NewContextOptions()
                .setBaseURL(config.baseUrl()));

    }

    @AfterEach
    void apiTearDown() {
        request.dispose();
    }

    @Test
    void apiUiStatusCode() {
        APIResponse apiResponseOk = request.get("/status_codes/200");
        APIResponse apiResponseNotFound = request.get("/status_codes/404");

        assertEquals(200, apiResponseOk.status());
        assertEquals(404, apiResponseNotFound.status());

        page().navigate(config.baseUrl() + "/status_codes");

        Locator statusOk = page().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("200"));
        Response uiResponseOk = page().waitForResponse(response -> response.url().contains("/status_codes/200"),
                () -> statusOk.click());

        assertEquals(200, uiResponseOk.status());

        page().goBack();

        Locator statusNotFound = page().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("404"));
        Response uiResponseNotFound = page().waitForResponse(response -> response.url().contains("/status_codes/404"),
                () -> statusNotFound.click());

        assertEquals(404, uiResponseNotFound.status());

        assertEquals(apiResponseOk.status(), uiResponseOk.status());
        assertEquals(apiResponseNotFound.status(), uiResponseNotFound.status());

    }

}
