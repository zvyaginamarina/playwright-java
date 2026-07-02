package tests.java;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;

public class MakeVideo {

    static BrowserContext contextWithVideo(Browser browser, String pathName) {
        BrowserContext context = browser.newContext(new NewContextOptions()
                .setRecordVideoDir(Paths.get(pathName)));
        return context;
    }

}
