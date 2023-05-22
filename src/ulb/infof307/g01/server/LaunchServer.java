package ulb.infof307.g01.server;

import io.github.cdimascio.dotenv.Dotenv;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

public class LaunchServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(Integer.parseInt(Dotenv.load().get("PORT")));
            server.start();

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
