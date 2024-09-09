import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderActions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserActions;
import java.util.List;
import constant.Endpoints;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCred.from;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> ingredients;
    private final int statusCode;
    private String accessToken;

    public OrderCreateTest(List<String> ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa73"), 200},
                {List.of("123", "123"), 500},
                {List.of(), 400}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        User user = new User("testuserQA@yandex.ru", "q1w2e3r4", "testuserQA");
        UserActions.create(user);
        Response loginResponse = UserActions.login(from(user));
        accessToken = loginResponse.path("accessToken");
    }
    @After
    public void removeUser() {
        if (accessToken != null) {
            UserActions.delete(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .statusCode(202)
                    .and()
                    .body("message", equalTo("User successfully removed"));
        }
    }
    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void checkOrderCreateWithAuth() {
        Order order= new Order(ingredients);
        OrderActions.createOrderByAuthorization(accessToken, order).then().assertThat()
                .statusCode(statusCode);
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void checkOrderCreateWithoutAuth() {
        Order order = new Order(ingredients);
        OrderActions.createOrderWithoutAuthorization(order).then().assertThat()
                .statusCode(statusCode);
    }

}