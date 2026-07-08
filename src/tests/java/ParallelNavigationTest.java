package tests.java;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelNavigationTest {

    private static boolean HEADLESS = System.getenv("CI") != null;

    private static Stream<Arguments> testParams() {

        List<String> browserList = new ArrayList<>();
        browserList.add("chromium");
        browserList.add("firefox");

        List<String> path = new ArrayList<>();

        path.add("/");
        path.add("/login");
        path.add("/dropdown");
        path.add("/javascript_alerts");
        path.add("/checkboxes");
        path.add("/hover");
        path.add("/status_codes");

        List<Arguments> list = new ArrayList<>();

        for (String browserValue : browserList) {
            for (String pathValue : path) {
                list.add(Arguments.of(browserValue, pathValue));
            }
        }

        return list.stream();

    };

    @ParameterizedTest
    @MethodSource("testParams")

    void testPageLoad(String browserName, String path) {
        Playwright playwright = Playwright.create();

        System.out.println("Test start in thread: " + Thread.currentThread().getName() + " at: " + LocalTime.now());

        BrowserType browserType = switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            default -> playwright.chromium();
        };

        Browser browser = browserType.launch(new LaunchOptions().setHeadless(HEADLESS));
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com" + path);

        assertThat(page).hasTitle(Pattern.compile(".*"));
        context.close();
        browser.close();
        playwright.close();
    }

}
