package ulb.infof307.g01.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.sql.SQLException;

public class DatabaseUsingTest {
    protected Database db = Database.singleton();
    protected File dbfile = new File("test.db");

    @BeforeEach
    void init() throws OpenedDatabaseException, SQLException {
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
