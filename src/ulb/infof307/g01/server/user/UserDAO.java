package ulb.infof307.g01.server.user;

import ulb.infof307.g01.server.database.Database;

import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    private final static Database database = Database.singleton();

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
