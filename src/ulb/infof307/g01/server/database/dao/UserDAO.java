package ulb.infof307.g01.server.database.dao;

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
}
