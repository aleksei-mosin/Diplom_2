package order;
import constant.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderActions {

    @Step("Создание заказа авторизованным пользователем")
    public static Response createOrderByAuthorization(String accessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(Endpoints.ORDER);
    }
    @Step("Создание заказа неавторизованным пользователем")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(Endpoints.ORDER);
    }
    @Step("Получение списка заказов")
    public static Response getListOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(Endpoints.ORDER);
    }
}
