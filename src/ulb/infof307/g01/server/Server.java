package ulb.infof307.g01.server;

import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;
import ulb.infof307.g01.server.handler.DeckRequestHandler;
import ulb.infof307.g01.server.handler.UserAccountHandler;
import ulb.infof307.g01.server.service.JWTService;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.port;

public class Server {

    private final Logger logger = Logger.getLogger("Server");
    private Database db = new Database();
    private final JWTService jwtService = new JWTService();
    private final int port;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public Server(int port) throws DatabaseException {
        this.port = port;
        initDatabase();
    }


    /* ====================================================================== */
    /*                              Server start                              */
    /* ====================================================================== */

    public void start() {
        logger.info("Starting server");
        port(port);
        get("/", (req, res) -> "You have reached the server");
        launchHandlers();
        logger.info("Server started on port " + port);
    }


    /* ====================================================================== */
    /*                             Database Init                              */
    /* ====================================================================== */

    private void initDatabase() throws DatabaseException {
        File dbfile = new File("demo.db");
        db = new Database();
        db.open(dbfile);
        db.initServerScheme();
    }

    private void launchHandlers() {
        new DeckRequestHandler(jwtService,db).init();
        new UserAccountHandler(jwtService, db).init();
    }
}
