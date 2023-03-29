package ulb.infof307.g01.gui.httpclient.dao;

import java.io.IOException;

public class UserDAOVerifier {
    public static void main(String[] args) throws IOException, InterruptedException {
        UserDAO userDAO = new UserDAO();
        // userDAO.register("guest", "guest");
        userDAO.login("guest", "guest");
    }
}
