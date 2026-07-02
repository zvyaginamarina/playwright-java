package tests.java;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class MakeScreenshot {

    static void screenShot(Page page, String screenShotPathName, String screenshotName) {

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get("target", "screenshots", screenShotPathName, screenshotName)));
    }

}
