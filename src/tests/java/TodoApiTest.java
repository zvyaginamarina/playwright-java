package tests.java;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

public class TodoApiTest {
    static APIRequestContext request;
    static Playwright playwright;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        APIRequest apiRequest = playwright.request();
        request = apiRequest.newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://jsonplaceholder.typicode.com/"));
    }

    @AfterAll
    static void tearDown() {
        request.dispose();
        playwright.close();
    }

    @Test
    void getObject() throws IOException {
        APIResponse response = request.get("todos/1");

        assertTrue(response.ok());

        ResponseObject responseValue = mapper.readValue(response.body(), ResponseObject.class);

        assertEquals(1, responseValue.userId);
        assertEquals(1, responseValue.id);
        assertEquals("delectus aut autem", responseValue.title);
        assertFalse(responseValue.completed);
    }
}

class ResponseObject {
    public int userId;
    public int id;
    public String title;
    public boolean completed;
}
