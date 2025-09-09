package tests;

import base.PageManager;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@DisplayName("Web Tests")
public class SearchTest extends PageManager {
    String target = "Филадельфия Дуэт",
            item = "филадельфия",
            scrollArgument = "420";

    @Test
    @DisplayName("Поиск товара")
    void testProductFindByName() {
        step("Открываем главную страницу", () -> {
            base.openMain();
        });
        step("Находим и открываем страницу товара", () -> {
            base.setItemSearch(item).
                    scrollAndSelectItem(target);
        });
        step("Проверяем отображаемый товар на странице", () -> {
            base.verifyItemProperty(target);
        });
    }
    //Arrange
    //Act
    //Assert
    @Test
    @DisplayName("Добавление товара в корину")
    void testCart() {
        String scrollArgument = "420";
        open("https://spb.setsushi.ru/menu/rolls/filadelfia-duet");
        $(".button_border").click();
        $(byText("Самовывоз")).click();

        //  ул.Оптиков, д.34 к1 лит А
        SelenideElement list = $(".pickup-option")
                .should(appear, Duration.ofSeconds(10));

        executeJavaScript("arguments[0].scrollTop += " + scrollArgument + ";", list);

        list.$$(".self-item__address")
                .findBy(text("ул.Оптиков, д.34 к1 лит А"))
                .shouldBe(visible, enabled)
                .click();

        $(byText("Выбрать")).click();
        $(".header_main_cart_button").click();
        $(".cart_item_info").shouldHave(Condition.text("Филадельфия Дуэт"), Duration.ofSeconds(10));
        sleep(10_000);
    }

    @Test
    void cartFillingTest() {
        String userUuid = "test-050920252";

        //ДОБАВЛЯЕМ КУКУ
        open("https://spb.setsushi.ru/setcoins-icon.svg");
        WebDriverRunner.getWebDriver().manage().addCookie(
                new Cookie("uuid", userUuid));

        //ЗАПОЛНЯЕМ КОРЗИНУ
        String body = "{\n" +
                "    \"menuItemId\": 627,\n" +
                "    \"type\": \"usual\",\n" +
                "    \"modificationId\": null,\n" +
                "    \"name\": \"Филадельфия Дуэт\",\n" +
                "    \"image\": \"https://static.setsushi.ru/images/collections/uploads/bg-optim/ea0584bc_optim_bg_e9edf0_w1000.jpg\"\n" +
                "}";

        String itemId = given()
                .contentType(ContentType.JSON)
                .queryParam("uuid", userUuid)
                .body(body)
                .post("https://api.setsushi.ru/web/basket/item")
                .then()
                .statusCode(200).extract().body().jsonPath().get("data.items[0].idsString");

        //САМ ТЕСТ С ПОДГОТОВОЛЕННОЙ КОРЗИНОЙ
        open("https://spb.setsushi.ru/cart");
        $(byText("Филадельфия Дуэт")).shouldBe(Condition.visible);

        $("[data-icon='times']").click();
        $(byText("Корзина пуста")).shouldBe(Condition.visible);
        //   sleep(10_000);

    }


}


