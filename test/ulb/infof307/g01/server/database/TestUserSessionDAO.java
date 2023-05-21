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

        db.initTables(DatabaseSchema.SERVER);
        this.userDAO = new UserDAO(this.db);
    }

    @Test
    void registerUser_SingleRegister_Success() throws DatabaseException {
        assertTrue(userDAO.registerUser("name", "password"));
    }

    @Test
    void registerUser_DoubleRegister_Fail() throws DatabaseException {
        userDAO.registerUser("name", "password");
        assertFalse(userDAO.registerUser("name", "password"));
    }

    @Test
    void loginUser_BeforeRegister_Fail() throws DatabaseException {
        assertFalse(userDAO.loginUser("name", "password"));
    }

    @Test
    void loginUser_AfterRegister_Success() throws DatabaseException {
        userDAO.registerUser("name", "password");
        assertTrue(userDAO.loginUser("name", "password"));
    }
}
