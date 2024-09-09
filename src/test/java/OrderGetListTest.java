import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderActions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;
import java.util.List;
import constant.Endpoints;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCred.from;

public class OrderGetListTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        User user = new User("testuserQA@yandex.ru", "q1w2e3r4", "testuserQA");
        UserActions.create(user);
        Response loginResponse = UserActions.login(from(user));
        accessToken = loginResponse.path("accessToken");
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa73"));
        OrderActions.createOrderByAuthorization(accessToken,order).then().assertThat()
                .statusCode(200);
    }
    @After
    @DisplayName("Удаление пользователя")
    public void removeUser() {
        if (accessToken != null) {
            UserActions.delete(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"))
                    .and()
                    .statusCode(202);
        }
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void checkOrderGetListWithAuth() {
        OrderActions.getListOrders(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void checkOrderGetListWithoutAuth() {
        OrderActions.getListOrders("").then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}