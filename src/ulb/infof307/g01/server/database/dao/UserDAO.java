package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO extends DAO {
    private final DatabaseAccess database;

    public UserDAO(DatabaseAccess database) {
        this.database = database;
    }

    public boolean userExists(UUID userId) throws DatabaseException {
        String sql = """
                    SELECT *
                    FROM user
                    WHERE user_id = ?
                """;

        ResultSet res = database.executeQuery(sql, userId.toString());
        return checkedNext(res);
    }

    public boolean usernameExists(String username) throws DatabaseException {
        String sql = """
                    SELECT *
                    FROM user
                    WHERE username = ?
                """;

        ResultSet res = database.executeQuery(sql, username);
        return checkedNext(res);
    }

    public String getUserSaltKey(String username) throws DatabaseException {
        String sql = """
                SELECT salt
                FROM user
                WHERE username = ?
                """;

        try (ResultSet res = database.executeQuery(sql, username)) {
            return res.getString("salt");

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public boolean loginUser(String username, String password) throws DatabaseException {
        if (!usernameExists(username))
            return false;

        User user = new User(username, password, getUserSaltKey(username));

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE username = ? AND password = ?
                )
                """;

        ResultSet res = database.executeQuery(sql,
                                              user.getUsername(),
                                              user.getPassword());

        return checkedNext(res);
    }

    public boolean registerUser(String username, String password) throws DatabaseException {
        if (usernameExists(username)) {
            System.out.println("Username already exists");
            return false;
        }

        User user = new User(username, password);
        UUID user_id = UUID.randomUUID();

        String sql = """
                INSERT INTO user (user_id, username, password, salt)
                VALUES (?, ?, ?, ?)
                """;

        database.executeUpdate(sql,
                               user_id.toString(),
                               user.getUsername(),
                               user.getPassword(),
                               user.getSaltString());
        return true;
    }

    public String getUserId(String username) {
        String sql = """
                SELECT user_id
                FROM user
                WHERE username = '%1$s'
                """;

        try (ResultSet res = database.executeQuery(sql, username)) {
            return res.getString("user_id");

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}

