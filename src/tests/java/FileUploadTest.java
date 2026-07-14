// // package tests.java;

// // import static org.junit.jupiter.api.Assertions.assertEquals;
// // import static org.junit.jupiter.api.Assertions.assertTrue;

// // import java.util.Base64;

// // import org.junit.jupiter.api.AfterAll;
// // import org.junit.jupiter.api.BeforeAll;
// // import org.junit.jupiter.api.Test;

// // import com.microsoft.playwright.APIRequest;
// // import com.microsoft.playwright.APIRequestContext;
// // import com.microsoft.playwright.APIResponse;
// // import com.microsoft.playwright.Playwright;
// // import com.microsoft.playwright.options.FormData;
// // import com.microsoft.playwright.options.RequestOptions;

// // public class FileUploadTest {
// tatic APIRequestContext request;
// tatic Playwright playwright;

// BeforeAll
// tatic void setUp() {
// right = Playwright.create();
// quest apiRequest = playwright.request();
// st = apiRequest.newContext(
// t.NewContextOptions()
// httpbin.org/"));
// 

// AfterAll
// tatic void tearDown() {
// st.dispose();
// right.close();
// 

// Test
// oid testFileUploadAndDownload() {
// грузка файла
// sponse uploadResponse = request.post("post",
// s.create().setMultipart(
// ("file", testFile)));

// оверка получения файла
// g responseBody = uploadResponse.text();
// tTrue(responseBody.contains("data:image/png;base64"));

// рификация содержимого
// g base64Data = responseBody.split("\"file\": \"")[1].split("\"")[0];
// ] receivedBytes = Base64.getDecoder().decode(base64Data.split(",")[1]);
// д...

// ачивание и проверка эталона
// sponse downloadResponse = request.get("image/png");
// д...

// оверка сигнатуры PNG
// ] content = downloadResponse.body();
// tEquals(0x89, content[0] & 0xFF);
// tEquals(0x50, content[1] & 0xFF);
// д...
// 

// // }
