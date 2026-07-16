package tests.java;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Route.FulfillOptions;

import net.datafaker.Faker;

public class FakerGenerTest extends BaseSetup {
    @Test
    void mockResponse() {
        Faker faker = new Faker();

        String fakeName = faker.name().fullName();

        page().route("**/dynamic_content", route -> {
            route.fulfill(new FulfillOptions()
                    .setBody("<html><body><h1>" + fakeName + "</h1></body></html>")
                    .setContentType("text/html")
                    .setStatus(200));
        });

        page().navigate("https://the-internet.herokuapp.com/dynamic_content");

        assertThat(page().getByText(fakeName)).isVisible();
    }
}
