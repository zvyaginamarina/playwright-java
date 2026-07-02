package tests.java;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;

public class MakeVideo {

    static BrowserContext contextWithVideo(Browser browser) {
        String videoPathName = CreateTimestampFolder.createFolder();

        BrowserContext contextForVideo = browser.newContext(new NewContextOptions()
                .setRecordVideoDir(Paths.get("target", "videos", videoPathName)));
        return contextForVideo;
    }

}
