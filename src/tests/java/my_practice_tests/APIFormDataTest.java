package tests.java.my_practice_tests;

import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;

import net.datafaker.Faker;

public class APIFormDataTest {

    static APIRequestContext request;
    static Playwright playwright;

    @BeforeAll
    static void browserSetUp() {
        playwright = Playwright.create();
        APIRequest apiRequest = playwright.request();
        request = apiRequest.newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://petstore.swagger.io/v2/"));
    }

    @AfterAll
    static void tearDown() {
        request.dispose();
        playwright.close();
    }

    @Test
    void uploadPetImage() {

        Faker faker = new Faker();

        int petId = faker.number().numberBetween(1, 100);
        String petName = "Doggo";
        String petStatus = "available";
        String petMetaData = "Very good boy";

        Pet pet1 = new Pet(petId, petName, petStatus);

        APIResponse petCreatedResponse = request.post("pet", RequestOptions.create().setData(pet1));
        assertTrue(petCreatedResponse.ok());

        FormData petImage = FormData.create()
                .set("additionalMetadata", petMetaData)
                .set("file", Paths.get("src", "tests", "resources", "doggo.jfif"));

        APIResponse petImageUploadedResponse = request.post("pet/" + petId + "/uploadImage",
                RequestOptions.create().setMultipart(petImage));
        System.out.println(petImageUploadedResponse.text());
        assertTrue(petImageUploadedResponse.ok());
        assertTrue(petImageUploadedResponse.text().contains(petMetaData));

    }
}
