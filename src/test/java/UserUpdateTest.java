import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;
import constant.Endpoints;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCred.from;

public class UserUpdateTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        User user = new User("testuserQA@yandex.ru", "q1w2e3r4", "testuserQA");
        UserActions.create(user);
        Response loginResponse = UserActions.login(from(user));
        accessToken = loginResponse.path("accessToken");
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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void checkUserUpdateDataWithAuth() {
        User userUpdate = new User("testuserQAnew@yandex.ru", "qazwsxedc123", "testuserQAnew");
        UserActions.authUserWithToken(accessToken, userUpdate)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void checkUserUpdateDataWithoutAuth() {
        User userUpdate = new User("testuser@yandex.ru", "qazwsxedc123", "testusernew");
        UserActions.authUserWithoutToken(userUpdate)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}