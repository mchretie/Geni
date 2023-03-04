package ulb.infof307.g01.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDatabase
{
    // clang-format off
    final String createStmt = """
        CREATE TABLE A (
            a    INTEGER    NOT NULL,
            b    TEXT       NOT NULL,
            PRIMARY KEY (a)
        )""";

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

    @AfterEach void teardown() throws SQLException
    {
        Database.singleton().close();
        dbname.delete();
    }

    @Test void executeQuery_NotOpened_ThrowsException()
    {
        assertThrows(DatabaseNotInitException.class,
                     () -> Database.singleton().executeQuery("dummy"));
    }

    @Test void executeUpdate_NotOpened_ThrowsException()
    {
        assertThrows(DatabaseNotInitException.class,
                     () -> Database.singleton().executeUpdate("dummy"));
    }

    @Test
    void open_TwiceOpened_ThrowsException()
      throws SQLException, OpenedDatabaseException
    {
        Database.singleton().open(dbname);
        assertThrows(OpenedDatabaseException.class,
                     () -> Database.singleton().open(dbname));
    }

    @Test
    void close_NonEmptyDB_DBFileExistsAfterClose()
      throws SQLException, DatabaseNotInitException, OpenedDatabaseException
    {
        Database.singleton().open(dbname);
        Database.singleton().executeUpdate(createStmt);
        Database.singleton().close();

        assertTrue(dbname.exists());
    }

    @Test
    void executeUpdate_TableCreated_TablePresent()
      throws SQLException, DatabaseNotInitException, OpenedDatabaseException
    {
        Database.singleton().open(dbname);
        Database.singleton().executeUpdate(createStmt);

        String stmt =
          "SELECT name FROM sqlite_master WHERE type='table' AND name='A'";
        ResultSet res = Database.singleton().executeQuery(stmt);

        assertTrue(res.next()); // table exists
    }

    @Test
    void executeUpdate_DataInserted_DataPresentAndRight()
      throws SQLException, DatabaseNotInitException, OpenedDatabaseException
    {
        Database.singleton().open(dbname);
        Database.singleton().executeUpdate(createStmt);
        Database.singleton().executeUpdate(insertStmt);

        ResultSet res = Database.singleton().executeQuery(queryStmt);
        res.next();

        assertEquals(-42, res.getInt("a"));
        assertEquals("intellij", res.getString("b"));
    }

    @Test
    void executeUpdate_DataDeleted_DataNotPresent()
      throws SQLException, DatabaseNotInitException, OpenedDatabaseException
    {
        Database.singleton().open(dbname);
        Database.singleton().executeUpdate(createStmt);
        Database.singleton().executeUpdate(insertStmt);
        Database.singleton().executeUpdate(delStmt);

        ResultSet res = Database.singleton().executeQuery(queryStmt);
        assertFalse(res.next());
    }
}
