package tests.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;

import config.EnvironmentConfig;

public class StatusCodeCombinedTest extends BaseSetup {

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

    @ParameterizedTest
    @ValueSource(ints = { 200, 404 })
    void apiUiStatusCode(int code) {
        APIResponse apiResponse = request.get("/status_codes/" + code);

        assertEquals(code, apiResponse.status());

        page().navigate(config.baseUrl() + "/status_codes");

        Locator statusLink = page().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(String.valueOf(code)));
        Response uiResponse = page().waitForResponse(response -> response.url().contains("/status_codes/" + code),
                () -> statusLink.click());

        assertEquals(code, uiResponse.status());

        assertEquals(apiResponse.status(), uiResponse.status());

    }

}
