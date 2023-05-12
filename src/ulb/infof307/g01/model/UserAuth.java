package ulb.infof307.g01.model;

public class UserAuth {
    private final String username;
    private final String password;

    public UserAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
