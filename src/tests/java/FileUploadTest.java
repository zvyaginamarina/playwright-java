package tests.java;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;

public class FileUploadTest {
    static APIRequestContext request;
    static Playwright playwright;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        APIRequest apiRequest = playwright.request();
        request = apiRequest.newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://httpbin.org/"));
    }

    @AfterAll
    static void tearDown() {
        request.dispose();
        playwright.close();
    }

    @Test
    void testFileUploadAndDownload() throws IOException {
        ImageGenerator generator = new ImageGenerator();
        byte[] image = generator.imgGenerator();
        FilePayload testFile = new FilePayload(
                "test.png",
                "image/png",
                image);

        APIResponse response = request.post(
                "post",
                RequestOptions.create().setMultipart(
                        FormData.create().set("file", testFile)));

        String responseBody = response.text();
        assertTrue(responseBody.contains("data:image/png;base64"));

        String base64Data = mapper.readTree(responseBody).get("files").get("file").asText();
        byte[] receivedBytes = Base64.getDecoder().decode(base64Data.split(",")[1]);

        assertArrayEquals(image, receivedBytes);

        APIResponse downloadResponse = request.get("image/png");
        byte[] content = downloadResponse.body();
        assertEquals(0x89, content[0] & 0xFF);
        assertEquals(0x50, content[1] & 0xFF);

        String contentType = downloadResponse.headers().get("content-type");
        assertEquals("image/png", contentType);
    }
}

class ImageGenerator {
    public byte[] imgGenerator() throws IOException {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] pngBytes = baos.toByteArray();
        return pngBytes;
    }
}
