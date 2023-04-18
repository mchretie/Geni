package ulb.infof307.g01.server;

import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.SQLException;

public class LaunchServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(8090);
            server.start();

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
