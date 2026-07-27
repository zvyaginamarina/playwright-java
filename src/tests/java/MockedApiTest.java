package tests.java;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Route.FulfillOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockedApiTest extends BaseSetup {

    private static ApiService apiService;
    static String mockedJson = """
            {
              "name": "Test User",
              "email": "test@example.com"
            }
            """;

    @BeforeAll
    static void mockSetUp() {
        apiService = mock(ApiService.class);
        when(apiService.fetchUserData()).thenReturn(mockedJson);
    }

    static class ApiService {
        public String fetchUserData() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String responseJson = """
                    {
                    "name": "Real User",
                    "email": "real@example.com"
                    }
                    """;
            return responseJson;
        }
    }

    @Test
    void testUserProfileWithMockedApi() {

        page().route("**/dynamic_content", route -> {
            String userData = apiService.fetchUserData();

            route.fulfill(new FulfillOptions().setBody(userData));

        });

        page().navigate("https://the-internet.herokuapp.com/dynamic_content");

        Locator responseBody = page().locator("pre");

        assertThat(responseBody).isVisible();
        assertThat(responseBody).containsText("Test User");
        assertThat(responseBody).containsText("test@example.com");

    }
}
