package tests.java;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class MakeScreenshot {

    static void screenShot(Page page, String pathName, String screenshotName) {

        page.screenshot(new ScreenshotOptions()
                .setPath(Paths.get(pathName, screenshotName)));
    }

}
