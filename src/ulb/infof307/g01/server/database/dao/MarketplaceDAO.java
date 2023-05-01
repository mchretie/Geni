package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketplaceDAO extends DAO {
    private final DatabaseAccess database;
    private DeckDAO deckDAO;

    public MarketplaceDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void setDeckDAO(DeckDAO deckDAO) {
        this.deckDAO = deckDAO;
    }

    private MarketplaceDeckMetadata extractMarketplaceDeckMetaData(ResultSet res) throws DatabaseException {
        try {
            Deck deck = deckDAO.extractDeckFrom(res);
            String owner_username = res.getString("username");
            int rating = res.getInt("rating");
            int downloads = res.getInt("downloads");

            return new MarketplaceDeckMetadata(deck, owner_username, rating, downloads);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void addDeckToMarketplace(UUID deckId) throws DatabaseException {
        String sql = """
                INSERT INTO marketplace (deck_id, rating, downloads)
                VALUES (?, ?, ?);
                """;

        database.executeUpdate(
                sql,
                deckId.toString(),
                String.valueOf(0),
                String.valueOf(0));
    }

    public void removeDeckFromMarketplace(UUID deckId) throws DatabaseException {
        String sql = """
                DELETE FROM marketplace
                WHERE deck_id = ?;
                """;

        database.executeUpdate(sql, deckId.toString());
    }

    public List<MarketplaceDeckMetadata> getMarketplaceDecksMetadata() throws DatabaseException {
        String sql = """
                SELECT D.deck_id, U.username, D.name, D.color, D.image, D.color_name, M.rating, M.downloads, 1 as 'public'
                FROM marketplace M
                INNER JOIN deck D ON M.deck_id = D.deck_id
                INNER JOIN user U ON U.user_id = D.user_id;
                """;

        ResultSet res = database.executeQuery(sql);
        List<MarketplaceDeckMetadata> decks = new ArrayList<>();
        while (checkedNext(res))
            decks.add(extractMarketplaceDeckMetaData(res));

        return decks;
    }

    private void incrementDownloads(UUID deckId) {
        String sql = """
                UPDATE marketplace
                SET downloads = downloads + 1
                WHERE deck_id = ?;
                """;

        database.executeUpdate(sql, deckId.toString());
    }

    public void addDeckToUserCollection(UUID deckId, UUID userId) throws DatabaseException {
        String sql = """
                INSERT INTO user_deck_collection (user_id, deck_id)
                VALUES (?, ?);
                """;

        database.executeUpdate(
                sql,
                userId.toString(),
                deckId.toString());

        incrementDownloads(deckId);
    }

    public void removeDeckFromUserCollection(UUID deckId, UUID userId) throws DatabaseException {
        String sql = """
                DELETE FROM user_deck_collection
                WHERE user_id = ? AND deck_id = ?;
                """;

        database.executeUpdate(sql,
                userId.toString(),
                deckId.toString());
    }

    public List<MarketplaceDeckMetadata> getSavedDecks(UUID userId) throws DatabaseException {
        String sql = """
                SELECT D.deck_id, U.username, D.name, D.color, D.image, D.color_name, M.rating, M.downloads, 1 as 'public'
                FROM marketplace M
                INNER JOIN deck D ON M.deck_id = D.deck_id
                INNER JOIN user U ON U.user_id = D.user_id
                INNER JOIN user_deck_collection C ON M.deck_id = C.deck_id
                WHERE C.user_id = ?;
                """;

        ResultSet res = database.executeQuery(sql, userId.toString());
        List<MarketplaceDeckMetadata> decks = new ArrayList<>();
        while (checkedNext(res))
            decks.add(extractMarketplaceDeckMetaData(res));

        return decks;
    }

    public int getNumberOfPublicPlayedDecks(UUID userId) throws DatabaseException {
        String sql = """
                SELECT COUNT(*) as count
                FROM marketplace
                INNER JOIN user_deck_score uds ON marketplace.deck_id = uds.deck_id
                WHERE user_id = ?;
                """;

        try (ResultSet res = database.executeQuery(sql, userId.toString())) {
            if (res.next())
                return res.getInt("count");

            return 0;

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }
}
