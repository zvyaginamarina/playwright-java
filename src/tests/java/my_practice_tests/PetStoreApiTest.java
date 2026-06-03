package tests.java.my_practice_tests;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import net.datafaker.Faker;

public class PetStoreApiTest {

    static APIRequestContext request;
    static Playwright playwright;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        playwright = Playwright.create();
        APIRequest apiRequest = playwright.request();
        request = apiRequest.newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://petstore.swagger.io/v2/")
                        .setExtraHTTPHeaders(Map.of("Content-Type", "application/json")));
    };

    @Test
    void getSoldPet() {
        APIResponse response = request.get("pet/findByStatus", RequestOptions.create()
                .setQueryParam("status", "sold"));

        assertTrue(response.ok());
        assertTrue(response.text().length() > 0);
    }

    @Test
    void createPetWithNameAndStatus() throws Exception {

        int petId = 1;
        String petName = "Doggo";
        String petStatus = "available";

        Pet pet1 = new Pet(petId, petName, petStatus);

        APIResponse response = request.post("pet", RequestOptions.create().setData(pet1));

        assertTrue(response.ok());

        Pet createdPet = mapper.readValue(response.body(), Pet.class);

        assertEquals(pet1.getId(), createdPet.getId());
        assertEquals(pet1.getName(), createdPet.getName());
        assertEquals(pet1.getStatus(), createdPet.getStatus());

    }

    @Test
    void createGetDeletePet() throws Exception {

        Faker faker = new Faker();

        Category category = new Category();
        category.setId(1);
        category.setName("Dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Big");

        Pet pet1 = new Pet();
        pet1.setId(faker.number().numberBetween(1, 20));
        pet1.setName(faker.dog().name());
        pet1.setStatus("pending");
        pet1.setCategory(category);
        pet1.setTags(List.of(tag));
        pet1.setPhotoUrls(List.of(
                "https://www.borrowmydoggy.com/_next/image?url=https%3A%2F%2Fcdn.sanity.io%2Fimages%2F4ij0poqn%2Fproduction%2Fe24bfbd855cda99e303975f2bd2a1bf43079b320-800x600.jpg&w=1080&q=80"));

        APIResponse petCreate = request.post("pet", RequestOptions.create().setData(pet1));
        assertTrue(petCreate.ok());

        Pet createdPet = mapper.readValue(petCreate.body(), Pet.class);

        APIResponse petGet = request.get("pet/" + createdPet.getId());
        assertTrue(petGet.ok());

        Pet fetchedPet = mapper.readValue(petGet.body(), Pet.class);

        assertEquals(pet1.getId(), fetchedPet.getId());
        assertEquals(pet1.getCategory().getId(), fetchedPet.getCategory().getId());
        assertEquals(pet1.getTags().get(0).getName(), fetchedPet.getTags().get(0).getName());
        assertEquals(pet1.getName(), fetchedPet.getName());

        APIResponse petDelete = request.delete("pet/" + createdPet.getId());

        assertTrue(petDelete.ok());

    }

    @AfterAll
    static void tearDown() {
        request.dispose();
        playwright.close();
    }

}
