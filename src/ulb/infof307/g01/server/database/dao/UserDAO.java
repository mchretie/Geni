package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.server.database.DatabaseAccess;

import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    private final DatabaseAccess database;

    public UserDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void registerGuest(UUID guestId) {
        String sql = """
            INSERT INTO user (user_id, username, password)
            VALUES ('%1$s', 'guest', 'guest')
            """.formatted(guestId.toString());

        database.executeUpdate(sql);
    }
}
