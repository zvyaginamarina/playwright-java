package tests.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PlaywrightSetupTest extends BasicTest {

    @Test
    void testPlaywrightSetup() {
        page.navigate("https://example.com");
        String title = page.title();
        assertEquals("Example Domain", title);
    }
}
