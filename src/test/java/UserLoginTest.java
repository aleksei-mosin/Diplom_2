import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;
import user.UserCred;
import constant.Endpoints;
import static org.hamcrest.Matchers.equalTo;
public class UserLoginTest {
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        User user = new User("testuserQA@yandex.ru", "q1w2e3r4", "testuserQA");
        UserActions.create(user);
    }
    @After
    @DisplayName("Удаление пользователя")
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
    @DisplayName("Логин под существующим пользователем")
    public void checkUserLogin() {
        User user = new User("testuserQA@yandex.ru", "q1w2e3r4", "testuserQA");
        Response loginResponse = UserActions.login(UserCred.from(user));
        accessToken = loginResponse.path("accessToken");

        Assert.assertEquals("Неверный статус код", 200, loginResponse.statusCode());
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void checkUserBadLoginPassword() {
        User user = new User("testuserQAbroken@yandex.ru", "q1w2e3r4", "testuserQA");
        Response response = UserActions.login(UserCred.from(user));
        accessToken = response.path("accessToken");
        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

}