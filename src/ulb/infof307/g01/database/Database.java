package ulb.infof307.g01.database;

import java.io.File;
import java.sql.*;

/**
 * Provide global access to database
 * <p>
 * It allows to execute queries and updates on a given database.
 * Be aware that before these, a database must’ve been open, otherwise
 * exceptions will be thrown.
 * <p>
 * At the moment, only one database can be open at a given time. Two databases
 * cannot be open at the same time.
 */
public class Database {
    private static Database instance;
    private Connection connection = null;

    private void assertOpened() throws DatabaseNotInitException {
        if (connection == null)
            throw new DatabaseNotInitException(
                    "Database must be opened before use");
    }

    /**
     * Get access to the Database object
     */
    public static Database singleton() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    /**
     * Open a database
     * <p>
     *
     * @param dbname filename used for the database
     */
    public void open(File dbname) throws OpenedDatabaseException, SQLException {
        if (connection != null) {
            throw new OpenedDatabaseException(
                    "Cannot open two databases at the same time");
        }

        connection = DriverManager
            .getConnection("jdbc:sqlite:" + dbname.toPath());
    }

    public void initTables(String[] tables) throws DatabaseNotInitException {
        executeUpdates(tables);
    }

    /**
     * Close the currently open database
     * <p>
     * End gracefully, before shutdown.
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Execute any SQL statement returning a result
     * <p>
     *
     * @param query a SQL statement to be executed
     * @return A java.sql.ResultSet with the resulting rows
     */
    public ResultSet executeQuery(String query)
            throws SQLException, DatabaseNotInitException {
        assertOpened();

        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute any SQL statement that is not returning a result
     * <p>
     * It will usually be for DELETE, INSERT and UPDATE.
     *
     * @param update a SQL statement to be executed
     */
    public void executeUpdate(String update) throws DatabaseNotInitException {
        assertOpened();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(update);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute multiple SQL statements that are not returning a result
     * <p>
     * It will usually be for DELETE, INSERT and UPDATE.
     *
     * @param updates a SQL statement to be executed
     */
    public void executeUpdates(String[] updates) throws DatabaseNotInitException {
        assertOpened();

        try {
            Statement stmt = connection.createStatement();
            for (String sql : updates)
                stmt.addBatch(sql);
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
