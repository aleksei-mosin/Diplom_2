import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserActions;

import constant.Endpoints;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
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
    @DisplayName("Создание уникального пользователя")
    public void checkUserCreate() {
        User user = new User("testuserQAuniq@yandex.ru", "q1w2e3r4", "testuserQA");
        Response response = UserActions.create(user);
        accessToken = response.path("accessToken");
        Assert.assertEquals("Неверный статус код", 200, response.statusCode());
    }
    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void checkDoubleUserCreate() {
        User user = new User("testuserQAuniq@yandex.ru", "q1w2e3r4", "testuserQA");
        Response response = UserActions.create(user);
        accessToken = response.path("accessToken");
        Assert.assertEquals("Неверный статус код", 200, response.statusCode());
        Response responseDoubleUser = UserActions.create(user);
        responseDoubleUser.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Создание пользователя без пароля")
    public void checkUserWithoutPasswordCreate() {
        User user = new User("testuserQAuniq@yandex.ru", "", "testuserQA");
        Response response = UserActions.create(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Создание пользователя без логина")
    public void checkUserWithoutLoginCreate() {
        User user = new User("", "q1w2e3r4", "testuserQA");
        Response response = UserActions.create(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}