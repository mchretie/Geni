package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.server.database.DatabaseConnectionManager;

import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    private final static DatabaseConnectionManager database = DatabaseConnectionManager.singleton();

    public UserDAO() {
    }

    public void registerGuest(UUID guestId) throws SQLException {
        String sql = """
            INSERT INTO user (user_id, username, password)
            VALUES ('%1$s', 'guest', 'guest')
            """.formatted(guestId.toString());

        database.executeUpdate(sql);
    }
}
