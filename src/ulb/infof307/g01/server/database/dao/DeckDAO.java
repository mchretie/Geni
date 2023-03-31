package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Save and retrieve decks from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Deck, apply changes
 * to it before saving it with saveDeck or deleteDeck.
 * <p>
 * Do not use directly, use the Database facade instead.
 * @see ulb.infof307.g01.server.database.Database
 */
public class DeckDAO extends DAO {

    private final DatabaseAccess database;
    private TagDAO tagDao;

    public DeckDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void setTagDao(TagDAO tagDao) {
        this.tagDao = tagDao;
    }

    /**
     * <p>
     * A deck is invalid if there exists a deck with
     * the same name but a different id in the database.
     * <p>
     * This may happen when a deck created outside
     * this class has the same name as one in the database.
     * This can be avoided by checking for uniqueness
     * beforehand.
     *
     * @see DeckDAO#deckNameExists
     */

    public boolean isDeckValid(Deck deck, UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id, name
                FROM deck
                WHERE NOT deck_id = ? AND user_id = ? AND name = ?
                """;

        return !checkedNext(database.executeQuery(sql,
                                                  deck.getId().toString(),
                                                  userId.toString(),
                                                  deck.getName()));
    }

    public boolean deckNameExists(String name) {
        String sql = """
                SELECT name
                FROM deck
                WHERE name = ?
                """;

        return checkedNext(database.executeQuery(sql, name));
    }


    /**
     * Write to long-term memory the contents of a deck
     * <p>
     * If the given deck is not valid, it will be ignored.
     *
     * @see DeckDAO#isDeckValid
     */
    public void saveDeck(Deck deck, UUID userId) throws DatabaseException {
        if (!isDeckValid(deck, userId))
            return;

        saveDeckIdentity(deck, userId);
        saveDeckTags(deck);
        saveDeckCards(deck);
    }

    /**
     * Get the deck identified by the given UUID
     * <p>
     * This should only be used by other managers.
     * To retrieve decks from memory, use getAllDecks
     * or other managers returning decks.
     * <p>
     *
     * @return The deck requested or null if it does not exist.
     */
    public Deck getDeck(UUID uuid) throws DatabaseException {
        String sql = """
                SELECT deck_id, name, color
                FROM deck
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, uuid.toString());
        if (!checkedNext(res))
            return null;
        return extractDeckFrom(res);
    }

    /**
     * Get multiple decks from their ids
     * <p>
     *
     * @param res result from a query on deck table
     * @return List of decks
     */
    public List<Deck> getDecks(List<UUID> res) throws DatabaseException {
        List<Deck> decks = new ArrayList<>();
        for (UUID deckId : res)
            decks.add(getDeck(deckId));
        return decks;
    }

    /**
     * Get all decks present in the database
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllDecks() throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                """;

        ResultSet res = database.executeQuery(sql);
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");
        return getDecks(deckIds);
    }

    /**
     * Get all decks associated with given user
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllUserDecks(UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                WHERE deck.user_id = ?
                """;


        ResultSet res = database.executeQuery(sql, userId.toString());
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");
        return getDecks(deckIds);
    }

    /**
     * Approximate search of decks with given search string
     *
     * @param userSearch query
     * @param userId user id
     * @return List of decks
     */
    public List<Deck> searchDecks(String userSearch, UUID userId) throws DatabaseException {
        String sql = """
                SELECT deck_id
                FROM deck
                WHERE user_id = ? AND name LIKE ?
                """;

        String pattern = userSearch + "%";
        ResultSet res = database.executeQuery(sql, userId.toString(), pattern);
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");

        return getDecks(deckIds);
    }

    /**
     * Delete a deck along with its cards from the database.
     */
    public void deleteDeck(UUID deckId, UUID userId) throws DatabaseException {
        String sql = """
                DELETE FROM deck
                WHERE deck_id = ? and user_id = ?
                """;

        database.executeUpdate(sql, deckId.toString(), userId.toString());
    }


    /**
     * Update a deck’s name
     * <p>
     * Upon the saving of multiple decks with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    private void saveDeckIdentity(Deck deck, UUID userId) throws DatabaseException {
        String sql = """
                INSERT INTO deck (deck_id, user_id, name, color)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(deck_id)
                DO UPDATE SET name = ?, color = ?
                ON CONFLICT(name)
                DO NOTHING
                """;

        database.executeUpdate(sql,
                               deck.getId().toString(),
                               userId.toString(),
                               deck.getName(),
                               deck.getColor(),

                               deck.getName(),
                               deck.getColor());
    }

    private void saveDeckTags(Deck deck) throws DatabaseException {
        tagDao.saveTagsFor(deck);
    }

    /**
     * Update the database with the data from the deck
     */
    @SuppressWarnings("unchecked")
    private void saveDeckCards(Deck deck) throws DatabaseException {
        HashSet<Card> currentCards = new HashSet<>(getCardsFor(deck.getId()));
        HashSet<Card> newCards = new HashSet<>(deck.getCards());

        Set<Card> addedCards = (Set<Card>) newCards.clone();
        addedCards.removeAll(currentCards);

        Set<Card> deletedCards = (Set<Card>) currentCards.clone();
        deletedCards.removeAll(newCards);

        for (Card addedCard : addedCards)
            saveCard(addedCard);

        for (Card deletedCard : deletedCards)
            deleteCard(deletedCard);
    }

    private void saveCard(Card card) throws DatabaseException {
        String sql = """
                INSERT INTO card (card_id, deck_id, front, back)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(card_id)
                DO UPDATE SET front = ?, back = ?
                """;

        database.executeUpdate(sql,
                               card.getId().toString(),
                               card.getDeckId().toString(),
                               card.getFront(),
                               card.getBack(),
                               card.getFront(),
                               card.getBack());
    }

    private void deleteCard(Card card) throws DatabaseException {
        String sql = """
                DELETE FROM card
                WHERE card.card_id = ?
                """;

        database.executeUpdate(sql, card.getId().toString());
    }

    private Card extractCardFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("card_id"));
            UUID deckId = UUID.fromString(res.getString("deck_id"));
            String front = res.getString("front");
            String back = res.getString("back");
            return new Card(uuid, deckId, front, back);
        } catch (SQLException e) {
            throw new DatabaseException((e.getMessage()));
        }
    }

    /**
     * Get all cards associated with given deck
     *
     * @return The cards of the deck. If the id
     * is not in the database, an empty list is
     * returned.
     */
    private List<Card> getCardsFor(UUID deckUuid) throws DatabaseException {
        String sql = """
                SELECT card_id, deck_id, front, back
                FROM card
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, deckUuid.toString());
        List<Card> cards = new ArrayList<>();
        while (checkedNext(res))
            cards.add(extractCardFrom(res));
        return cards;
    }

    private Deck extractDeckFrom(ResultSet res) throws DatabaseException {
        try {
            UUID uuid = UUID.fromString(res.getString("deck_id"));
            String name = res.getString("name");
            String color = res.getString("color");
            List<Card> cards = getCardsFor(uuid);
            List<Tag> tags = tagDao.getTagsFor(uuid);

            return new Deck(name, uuid, cards, tags, color);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
