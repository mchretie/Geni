package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserSessionDAO extends DatabaseUsingTest {

    UserDAO userDAO;

    @Override
    @BeforeEach
    void init() throws DatabaseException {
        super.init();

        db.initTables(DatabaseScheme.SERVER);
        this.userDAO = new UserDAO(this.db);
    }

    @Test
    void registerUser_SingleRegister_Success() {
        assertTrue(userDAO.registerUser("name", "password"));
    }

    @Test
    void registerUser_DoubleRegister_Fail() {
        userDAO.registerUser("name", "password");
        assertFalse(userDAO.registerUser("name", "password"));
    }

    @Test
    void loginUser_BeforeRegister_Fail() {
        assertFalse(userDAO.loginUser("name", "password"));
    }

    @Test
    void loginUser_AfterRegister_Success() {
        userDAO.registerUser("name", "password");
        assertTrue(userDAO.loginUser("name", "password"));
    }
}
