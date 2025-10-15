package tests;

import com.codeborne.selenide.WebDriverConditions;
import config.TestBase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CityTest extends TestBase {
    @Test
    @Tag("city")
    void testCityUrl() {
        open("https://setsushi.ru/");
        $(".region-label_city").click();
        $(byText("Санкт-Петербург")).click();
        $(".region-label_city").shouldHave(text("Санкт-Петербург"));
        webdriver().shouldHave(WebDriverConditions.url("https://spb.setsushi.ru/"));
        sleep(3_000);
    }
}
