package user;
import constant.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserActions {
    @Step("Создание пользователя")
    public static Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("Авторизация пользователя")
    public static Response login(UserCred userCred) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCred)
                .when()
                .post(Endpoints.LOGIN);
    }

    @Step("Удаление пользователя")
    public static Response delete(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete(Endpoints.USER);
    }
    @Step("Авторизация пользователя с токеном")
    public static Response authUserWithToken(String accessToken, User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(Endpoints.USER);
    }
    @Step("Авторизация пользователя без токена")
    public static Response authUserWithoutToken(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(Endpoints.USER);
    }
}
