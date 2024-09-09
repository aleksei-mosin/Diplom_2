package user;

public class UserCred {
    private final String email;
    private final String password;

    public UserCred(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCred from(User user) {
        return new UserCred(user.getEmail(), user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CourierCreds {" +
                "login='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
