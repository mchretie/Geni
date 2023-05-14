package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.rating.RatingValue;
import ulb.infof307.g01.model.rating.UserRating;
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
            int rating = getRatingFor(deck.getId());
            int downloads = res.getInt("downloads");

            return new MarketplaceDeckMetadata(deck, owner_username, rating, downloads);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    public UserRating getUserRating(UUID deckId, UUID userId) throws DatabaseException {
        String sql = """
                    SELECT value
                    FROM user_rating
                    WHERE deck_id = ? AND user_id = ?;
                """;

        try {
            ResultSet res = database.executeQuery(sql,
                                                  deckId.toString(),
                                                  userId.toString());

            RatingValue rating = UserRating.DEFAULT_VALUE;
            if (checkedNext(res)) {
                rating = RatingValue.fromInt(res.getInt("value"));
            }

            return new UserRating(deckId, userId, rating);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void addRating(UserRating userRating) throws DatabaseException {
        String sql = """                                                                                                                                  
                INSERT INTO user_rating (deck_id, user_id, value)
                VALUES (?, ?, ?)
                ON CONFLICT(deck_id, user_id)
                DO UPDATE SET value = ?
            """;

        database.executeUpdate(sql,
                               userRating.deckId().toString(),
                               userRating.userId().toString(),
                               String.valueOf(userRating.value().asInt()),
                               String.valueOf(userRating.value().asInt()));
    }

    private int getRatingFor(UUID deckId) throws DatabaseException {
        String sql = """
                SELECT value
                FROM user_rating
                WHERE deck_id = ?;
                """;

        try {
            ResultSet res = database.executeQuery(sql,
                                                  deckId.toString());

            int ratingsCount = 0;
            int ratingsSum = 0;

            while (checkedNext(res)) {
                ratingsCount++;
                ratingsSum += res.getInt("value");
            }

            int userRating = UserRating.DEFAULT_VALUE.asInt();
            if (ratingsCount != 0)
                userRating = Math.round((float) ratingsSum / ratingsCount);

            return userRating;

        } catch (SQLException e) {
            throw new DatabaseException("SQLError: " + e.getMessage());
        }
    }

    public void addDeckToMarketplace(UUID deckId) throws DatabaseException {
        String sql = """
                INSERT INTO marketplace (deck_id, downloads)
                VALUES (?, ?);
                """;

        database.executeUpdate(
                sql,
                deckId.toString(),
                String.valueOf(0));

        setDeckPublic(deckId, true);
    }

    public void removeDeckFromMarketplace(UUID deckId) throws DatabaseException {
        String sql = """
                DELETE FROM marketplace
                WHERE deck_id = ?;
                """;

        database.executeUpdate(sql, deckId.toString());
        setDeckPublic(deckId, false);
    }

    public void removeDeckFromNonOwnerCollection(UUID deckId, UUID ownerId) throws DatabaseException {
        String sql = """
                DELETE FROM user_deck_collection
                WHERE deck_id = ? AND user_id != ?;
                """;

        database.executeUpdate(sql, deckId.toString(), ownerId.toString());
    }

    public void setDeckPublic(UUID deckId, boolean isPublic) throws DatabaseException {
        String sql = """
                UPDATE deck
                SET public = ?
                WHERE deck_id = ?;
                """;

        database.executeUpdate(sql, isPublic ? 1 : 0, deckId.toString());
    }

    public List<MarketplaceDeckMetadata> getMarketplaceDecksMetadata() throws DatabaseException {
        String sql = """
                SELECT D.deck_id, U.username, D.name, D.color, D.image, D.color_name, M.downloads, 1 as 'public'
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

    private void incrementDownloads(UUID deckId) throws DatabaseException {
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
                VALUES (?, ?)
                ON CONFLICT DO NOTHING;
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
                SELECT D.deck_id, U.username, D.name, D.color, D.image, D.color_name, M.downloads, 1 as 'public'
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
