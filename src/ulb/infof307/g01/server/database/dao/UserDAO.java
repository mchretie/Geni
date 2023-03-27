package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.User;
import ulb.infof307.g01.server.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    private final static Database database = Database.singleton();

    public void registerGuest(UUID guestId) throws SQLException {
        String sql = """
                INSERT INTO user (user_id, username, password)
                VALUES ('%1$s', 'guest', 'guest')
                """.formatted(guestId.toString());

        database.executeUpdate(sql);
    }

    public boolean userExists(UUID userId) throws SQLException {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE user_id = '%1$s'
                )
                """.formatted(userId.toString());

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getBoolean(1);
        }
    }

    public boolean usernameExists(String username) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE username = '%1$s'
                )
                """.formatted(username);

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getBoolean(1);
        } catch (SQLException e) {
            return false;
        }
    }

    public String getUserSaltKey(String username) {
        String sql = """
                SELECT salt
                FROM user
                WHERE username = '%1$s'
                """.formatted(username);

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getString(1);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean loginUser(String username, String password) {
        if (!usernameExists(username)) return false;

        User user = new User(username, password, getUserSaltKey(username));

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM user
                    WHERE username = '%1$s' AND password = '%2$s'
                )
                """.formatted(username, user.getPassword());

        try (ResultSet res = database.executeQuery(sql)) {
            return res.getBoolean(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        if (usernameExists(username)) return false;

        User user = new User(username, password);
        UUID user_id = UUID.randomUUID();

        String sql = """
                INSERT INTO user (user_id, username, password, salt)
                VALUES ('%1$s', '%2$s', '%3$s', '%4$s')
                """.formatted(user_id.toString(), user.getUsername(), user.getPassword(), user.getSaltString());

        try {
            database.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

