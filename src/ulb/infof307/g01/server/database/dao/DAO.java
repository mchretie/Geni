package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Useful base that DAOs should use
 */
public abstract class DAO {
    protected boolean checkedNext(ResultSet rs) throws DatabaseException {
        try {
            return rs.next();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    protected List<UUID> extractUUIDsFrom(ResultSet res, String colName) {
        try {
            List<UUID> uuids = new ArrayList<>();
            while (res.next())
                uuids.add(UUID.fromString(res.getString(colName)));
            return uuids;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
