package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.server.database.DatabaseConnectionManager;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabaseConnectionManager {
    // clang-format off
    final String[] createStmt = new String[]{
            """
        CREATE TABLE A (
            a    INTEGER    NOT NULL,
            b    TEXT       NOT NULL,
            PRIMARY KEY (a)
        )"""
    };

    final String insertStmt = """
            INSERT INTO A (a, b)
            VALUES (42, 'nvim'), (-42, 'intellij')
            """;

    final String delStmt = """
            DELETE FROM A
            WHERE b = 'intellij'
            """;

    final String queryStmt = """
            SELECT a, b FROM A
            WHERE b = 'intellij'
            """;

    File dbname = new File("test.db");
    // clang-format on

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void teardown() throws SQLException {
        DatabaseConnectionManager.singleton().close();
        dbname.delete();
    }

    @Test
    void executeQuery_NotOpened_ThrowsException() {
        assertThrows(DatabaseException.class,
                () -> DatabaseConnectionManager.singleton().executeQuery("dummy"));
    }

    @Test
    void executeUpdate_NotOpened_ThrowsException() {
        assertThrows(DatabaseException.class,
                () -> DatabaseConnectionManager.singleton().executeUpdate("dummy"));
    }

    @Test
    void open_TwiceOpened_ThrowsException()
            throws SQLException, DatabaseException {
        DatabaseConnectionManager.singleton().open(dbname);
        assertThrows(DatabaseException.class,
                () -> DatabaseConnectionManager.singleton().open(dbname));
    }

    @Test
    void close_NonEmptyDB_DBFileExistsAfterClose()
            throws SQLException, DatabaseException {
        DatabaseConnectionManager.singleton().open(dbname);
        DatabaseConnectionManager.singleton().initTables(createStmt);
        DatabaseConnectionManager.singleton().close();

        assertTrue(dbname.exists());
    }

    @Test
    void executeUpdate_TableCreated_TablePresent()
            throws SQLException, DatabaseException {
        DatabaseConnectionManager.singleton().open(dbname);
        DatabaseConnectionManager.singleton().initTables(createStmt);

        String stmt =
                "SELECT name FROM sqlite_master WHERE type='table' AND name='A'";
        ResultSet res = DatabaseConnectionManager.singleton().executeQuery(stmt);

        assertTrue(res.next()); // table exists
    }

    @Test
    void executeUpdate_DataInserted_DataPresentAndRight()
            throws SQLException, DatabaseException {
        DatabaseConnectionManager.singleton().open(dbname);
        DatabaseConnectionManager.singleton().initTables(createStmt);
        DatabaseConnectionManager.singleton().executeUpdate(insertStmt);

        ResultSet res = DatabaseConnectionManager.singleton().executeQuery(queryStmt);
        res.next();

        assertEquals(-42, res.getInt("a"));
        assertEquals("intellij", res.getString("b"));
    }

    @Test
    void executeUpdate_DataDeleted_DataNotPresent()
            throws SQLException, DatabaseException {
        DatabaseConnectionManager.singleton().open(dbname);
        DatabaseConnectionManager.singleton().initTables(createStmt);
        DatabaseConnectionManager.singleton().executeUpdate(insertStmt);
        DatabaseConnectionManager.singleton().executeUpdate(delStmt);

        ResultSet res = DatabaseConnectionManager.singleton().executeQuery(queryStmt);
        assertFalse(res.next());
    }
}
