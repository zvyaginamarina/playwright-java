package tests.java.my_practice_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.WebSocketFrame;
import com.microsoft.playwright.WebSocketRoute;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

public class WebSocketTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    List<WebSocketFrame> sentFrames = new ArrayList<>();
    List<WebSocketFrame> receivedFrames = new ArrayList<>();

    boolean wsClosed = false;

    @BeforeAll
    static void browserSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void contextSetUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void contextTearDown() {
        context.close();
    }

    @AfterAll
    static void browserTearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void wsMonitoring() {

        page.onWebSocket(ws -> {
            System.out.println("WS opened " + ws.url());

            ws.onFrameSent(frame -> sentFrames.add(frame));

            ws.onFrameReceived(frame -> receivedFrames.add(frame));

            ws.onClose(closedWs -> System.out.println("WS closed"));
        });

        page.waitForWebSocket(() -> page.navigate("https://websocketstest.com/"));
        page.getByPlaceholder("start typing name here...").fill("Hello world!");
        page.locator("#version").fill("1");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Send")).click();

        page.waitForTimeout(5000);

        for (WebSocketFrame frame : sentFrames) {
            System.out.println(frame.text());
        }

        for (WebSocketFrame frame : receivedFrames) {
            System.out.println(frame.text());
        }

        assertFalse(sentFrames.isEmpty());
        assertFalse(receivedFrames.isEmpty());

    }

    @Test
    void fullWSConnectionAudit() {
        page.onWebSocket(ws -> {
            System.out.println("WS opened " + ws.url());
            ws.onFrameSent(frame -> sentFrames.add(frame));
            ws.onFrameReceived(frame -> receivedFrames.add(frame));
            ws.onClose(closedWs -> {
                wsClosed = ws.isClosed();
            });
        });
        page.waitForWebSocket(() -> page.navigate("https://websocketstest.com/"));
        page.getByPlaceholder("start typing name here...").fill("Hello world!");
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Send")).click();

        page.waitForTimeout(5000);

        for (WebSocketFrame frame : sentFrames) {
            System.out.println("SENT: " + frame.text());
        }

        for (WebSocketFrame frame : receivedFrames) {
            System.out.println("RECIEVED: " + frame.text());
        }

        System.out.println("Sent frames count: " + sentFrames.size());
        System.out.println("Recieved frames count: " + receivedFrames.size());

        // в тесте соединение WS не будет закрыто, поэтому в консоли всегда будет WS
        // wasn't closed
        if (wsClosed == false) {
            System.out.println("WS wasn't closed");
        } else {
            System.out.println("WS closed");
        }

        assertFalse(sentFrames.isEmpty());
        assertFalse(receivedFrames.isEmpty());
    }

    @Test
    void wsMockup() {

        String mockupMessage = "Hello world";
        String pageMessage = "hello from page";

        page.routeWebSocket("wss://echo.websocket.org", route -> {
            route.onMessage(frame -> {
                if (pageMessage.equals(frame.text())) {
                    route.send(mockupMessage);
                }
            });
        });

        page.navigate("file:///D:/Java/playwright-java/src/tests/resources/ws-mock-practice.html");

        assertThat(page.locator("#output")).containsText(mockupMessage);
        assertThat(page.locator("#output")).not().containsText(pageMessage);

    }

    @Test
    void wsChangeServerAnswer() {

        String mockupMessage = "Hello world";

        page.routeWebSocket("wss://echo.websocket.org", route -> {
            WebSocketRoute server = route.connectToServer();
            server.onMessage(frame -> route.send(mockupMessage));
        });

        page.navigate("file:///D:/Java/playwright-java/src/tests/resources/ws-mock-practice.html");

        assertThat(page.locator("#output")).containsText(mockupMessage);

    }
}
