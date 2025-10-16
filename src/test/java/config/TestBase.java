package config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class TestBase {
    @BeforeAll
    static void configure() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        DesiredCapabilities capabilities = new DesiredCapabilities();

        Configuration.browserCapabilities = capabilities;
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browser_size", "1920x1080");
        Configuration.pageLoadStrategy = "eager";

        String remote = System.getProperty("remote"); // возьмём то, что передали из Gradle
        String browserVersion = System.getProperty("browser_version");

        if (remote != null && !remote.isEmpty()) {
            Configuration.remote = remote;

            Map<String, Object> selenoid = new HashMap<>();
            selenoid.put("enableVNC", true);
            selenoid.put("enableVideo", true);

            capabilities.setCapability("selenoid:options", selenoid);
        }

        if (browserVersion != null && !browserVersion.isEmpty()) {
            Configuration.browserVersion = browserVersion;
        }
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();

        String remote = System.getProperty("remote");
        if (remote != null && !remote.isEmpty()) {
            Attach.addVideo();
        }
    }

}
