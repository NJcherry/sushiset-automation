package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    private final static SelenideElement SEARCH_INPUT = $(".header_main_search_input"),
            SEARCH_TITLE_LIST = $(".header_main_search_list .ps");

    private final static String ITEM_NAME = ".header_main_search_option_name p",
            SEARCH_OPTION = ".header_main_search_option";

    @Step("https://setsushi.ru")
    public BasePage openMain() {
        open("https://setsushi.ru/");
        return this;
    }

    @Step("Ищем товар \"{name}\" в строке поиска")
    public BasePage setItemSearch(String name) {
        SEARCH_INPUT.click();
        SEARCH_INPUT.setValue(name);
        return this;
    }

    @Step("Прокручиваем список и выбираем нужный товар \"{target}\"")
    public BasePage scrollAndSelectItem(String target) {
        SelenideElement list = SEARCH_TITLE_LIST
                .should(appear, Duration.ofSeconds(10));
        String scrollArgument = "220";

        executeJavaScript("arguments[0].scrollTop += " + scrollArgument + ";", list);

        list.$$(ITEM_NAME)
                .findBy(text(target))
                .closest(SEARCH_OPTION)
                .shouldBe(visible, enabled)
                .click();
        return this;
    }
}
