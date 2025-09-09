package tests;

import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class CartExampleTest {


    @Test
    void cartFillingTest(){
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
        $(byText("Филадельфия Дуэт")).shouldBe(visible);

      //  ОЧИЩАЕМ КОРЗИНУ
        given()
                .contentType(ContentType.JSON)
                .queryParam("uuid", userUuid)
                .delete("https://api.setsushi.ru/web/basket/items/"+itemId+"/delete")
                .then()
                .statusCode(200);

    }

}
