package tests;

import base.PageManager;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@DisplayName("Web Tests")
public class SearchTest extends PageManager {
    String target = "Филадельфия Дуэт",
            name = "филадельфия",
            pickupAddress = "ул.Оптиков, д.34 к1 лит А";

    @Test
    @DisplayName("Поиск товара")
    @Tag("search")
    void testProductFindByName() {
        step("Открываем главную страницу", () -> {
            base.openMain();
        });
        step("Находим и открываем страницу товара", () -> {
            base.setItemSearch(name).
                    scrollAndSelectItem(target);
        });
        step("Проверяем отображаемый товар на странице", () -> {
            item.verifyItemProperty(target);
        });
    }
    //Arrange
    //Act
    //Assert
    @Test
    @DisplayName("Добавление товара в корину")
    void testCart() {
        step("Открываем главную страницу", () -> {
            base.openMain();
        });
        step("Находим и открываем страницу товара", () -> {
            base.setItemSearch(name).
                    scrollAndSelectItem(target);
        });
        step("Нажимаем на кнопку добавления товара в корзину", () -> {
            item.addItemToCart();
        });
        step("Выбираем Самовывоз", () -> {
            item.choosePickupOption();
        });
        step("Выбираем адрес для самовывоза", () -> {
            item.scrollAndSelectAddress();
        });
        step("Проверяем наличие выбранного товара в корзине", () -> {
            item.verifyItemInCart(target);
        });
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


