package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ItemPage {
    SelenideElement ADD_ITEM_TO_CART_BUTTON = $(".button_border"),
    PICKUP_BUTTON = $(byText("Самовывоз")),
    PICKUP_OPTIONS_LIST = $(".pickup-option"),
    CHOOSE_BUTTON = $(byText("Выбрать")),
    CART_BUTTON = $(".header_main_cart_button"),
    CART_ITEM_INFO = $(".cart_item_info");

    String PICKUP_ADDRESS = "ул.Оптиков, д.34 к1 лит А";

    @Step("Проверяем соответствие найденного товара на странице")
    public ItemPage verifyItemProperty(String target) {
        $("h1").shouldHave(Condition.text(target));
        return this;
    }

    @Step("Жмем на кнопку добавления товара в корзину")
    public ItemPage addItemToCart() {
        ADD_ITEM_TO_CART_BUTTON.click();
        return this;
    }

    @Step("Выбираем опцию \"Самовывоз\"")
    public ItemPage choosePickupOption() {
        PICKUP_BUTTON.click();
        return this;
    }

    @Step("Прокручиваем список и выбираем нужный адрес \"{pickupAddress}\"")
    public ItemPage scrollAndSelectAddress() {
        String scrollArgument = "420";
        SelenideElement list = PICKUP_OPTIONS_LIST.should(appear, Duration.ofSeconds(10));
        executeJavaScript("arguments[0].scrollTop += " + scrollArgument + ";", list);
        PICKUP_OPTIONS_LIST.$$(".self-item__address")
                .findBy(text(PICKUP_ADDRESS))
                .shouldBe(visible, enabled)
                .click();
        CHOOSE_BUTTON.click();
        return this;
    }

    @Step("Проверяем наличие товара в корзине")
    public ItemPage verifyItemInCart(String target) {
        CART_BUTTON.click();
        CART_ITEM_INFO.shouldHave(Condition.text(target), Duration.ofSeconds(10));
        sleep(10_000);
        return this;
    }
}
