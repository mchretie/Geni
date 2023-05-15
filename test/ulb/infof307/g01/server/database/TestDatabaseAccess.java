package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabaseAccess {
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
    DatabaseAccess db = new DatabaseAccess();

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void teardown() throws DatabaseException {
        db.close();
        dbname.delete();
    }

    @Test
    void executeQuery_NotOpened_ThrowsException() {
        assertThrows(DatabaseException.class,
                () -> db.executeQuery("dummy"));
    }

    @Test
    void executeUpdate_NotOpened_ThrowsException() {
        assertThrows(DatabaseException.class,
                () -> db.executeQuery("dummy"));
    }

    @Test
    void open_TwiceOpened_ThrowsException() throws DatabaseException {
        db.open(dbname);
        assertThrows(DatabaseException.class, () -> db.open(dbname));
    }

    @Test
    void close_NonEmptyDB_DBFileExistsAfterClose() throws DatabaseException {
        db.open(dbname);
        db.initTables(createStmt);
        db.close();

        assertTrue(dbname.exists());
    }

    @Test
    void executeUpdate_TableCreated_TablePresent()
            throws SQLException, DatabaseException {

        db.open(dbname);
        db.initTables(createStmt);

        String sql = """
                SELECT name
                FROM sqlite_master
                WHERE type='table' AND name='A'
                """;

        try (ResultSet res = db.executeQuery(sql)) {
            assertTrue(res.next()); // table exists
        }
    }

    @Test
    void executeUpdate_DataInserted_DataPresentAndRight()
            throws SQLException, DatabaseException {

        db.open(dbname);
        db.initTables(createStmt);
        db.executeUpdate(insertStmt);

        try (ResultSet res = db.executeQuery(queryStmt)) {
            res.next();

            assertEquals(-42, res.getInt("a"));
            assertEquals("intellij", res.getString("b"));
        }
    }

    @Test
    void executeUpdate_DataDeleted_DataNotPresent()
            throws SQLException, DatabaseException {

        db.open(dbname);
        db.initTables(createStmt);
        db.executeUpdate(insertStmt);
        db.executeUpdate(delStmt);

        try (ResultSet res = db.executeQuery(queryStmt)) {
            assertFalse(res.next());
        }
    }
}
