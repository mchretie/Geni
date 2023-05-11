package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;

public class DatabaseUser {
    protected DatabaseAccess db;
    protected File dbfile = new File("test.db");

    @BeforeEach
    void init() throws DatabaseException {
        db = new DatabaseAccess();
        assert !dbfile.exists() || dbfile.delete();
        db.open(dbfile);
    }

    @AfterEach
    void teardown() {
        db.close();
        assert dbfile.delete();
    }
}