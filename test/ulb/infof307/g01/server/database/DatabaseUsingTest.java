package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ulb.infof307.g01.server.database.DatabaseConnectionManager;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;
import java.sql.SQLException;

public class DatabaseUsingTest {
    protected DatabaseConnectionManager db = DatabaseConnectionManager.singleton();
    protected File dbfile = new File("test.db");

    @BeforeEach
    void init() throws DatabaseException, SQLException {
        if (dbfile.exists()) {
            dbfile.delete();
        }
        db.open(dbfile);
    }

    @AfterEach
    void teardown() throws SQLException {
        db.close();
        dbfile.delete();
    }
}
