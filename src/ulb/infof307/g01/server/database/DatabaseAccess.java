package ulb.infof307.g01.server.database;


import org.sqlite.SQLiteConfig;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;
import java.sql.*;

/**
 * Provide access to database
 * <p>
 * It allows to execute queries and updates on a given database.
 * Be aware that before these, a database must’ve been open, otherwise
 * exceptions will be thrown.
 * <p>
 * At the moment, only one database can be open at a given time. Two databases
 * cannot be open at the same time.
 */
public class DatabaseAccess {
    private Connection connection = null;

    /* ====================================================================== */
    /*                        Database management                             */
    /* ====================================================================== */

    /**
     * Open a database
     * <p>
     *
     * @param dbname filename used for the database
     */
    public void open(File dbname) throws DatabaseException {
        try {
            if (connection != null) {
                String msg = "Cannot open two databases at the same time";
                throw new DatabaseException(msg);
            }

            String url = "jdbc:sqlite:" + dbname.toPath();
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(url, config.toProperties());

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Close the currently open database
     * <p>
     * End gracefully, before shutdown.
     */
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Initialize the database scheme
     * <p>
     * The array of create statements should be taken from
     * DatabaseScheme.
     * </p>
     *
     * @param tables set of updates to init the tables of the database
     * @see DatabaseSchema
     */
    public void initTables(String[] tables) throws DatabaseException {
        executeUpdates(tables);
    }

    /**
     * Execute any SQL statement returning a result
     * <p>
     * A variable number of arguments may be passed. These will replace
     * the '?' occurrences in the query.
     *
     * @param query a SQL statement to be executed
     * @param args  the arguments to be replaced in the query
     * @return A java.sql.ResultSet with the resulting rows
     */
    public ResultSet executeQuery(String query, Object... args)
            throws DatabaseException {
        try {
            assertOpened();

            // This should not (!) be inside a try-with-resource
            // because if it is, the ResultSet from the query
            // will be closed before the caller processes it.
            return createStatement(query, args).executeQuery();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Execute any SQL statement that is not returning a result
     * <p>
     * It will usually be for DELETE, INSERT and UPDATE.
     * <p>
     * A variable number of arguments may be passed. These will replace
     * the '?' occurrences in the query.
     *
     * @param update a SQL statement to be executed
     * @param args   the arguments to be replaced in the update
     */
    public void executeUpdate(String update, Object... args)
            throws DatabaseException {
        try {
            assertOpened();
            try (PreparedStatement s = createStatement(update, args)) {
                s.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Execute multiple SQL statements that are not returning a result
     * <p>
     * It will usually be for DELETE, INSERT and UPDATE.
     *
     * @param updates a SQL statement to be executed
     */
    public void executeUpdates(String[] updates) throws DatabaseException {
        assertOpened();
        try (Statement stmt = connection.createStatement()) {
            for (String sql : updates)
                stmt.addBatch(sql);
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    /* ====================================================================== */
    /*                        Helper methods                                  */
    /* ====================================================================== */

    private void assertOpened() throws DatabaseException {
        if (connection == null)
            throw new DatabaseException("Database must be opened before use");
    }

    private PreparedStatement createStatement(String sql, Object... args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof String arg)
                statement.setString(i + 1, arg);
            else if (args[i] instanceof Integer arg)
                statement.setInt(i + 1, arg);
            else
                throw new DatabaseException("Unsupported argument type for query");
        }
        return statement;
    }
}
