package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.server.database.DatabaseConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Save and retrieve decks from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Deck, apply changes
 * to it before saving it with saveDeck or deleteDeck.
 */
public class DeckDAO {


    private final static DatabaseConnectionManager database = DatabaseConnectionManager.singleton();
    private TagDAO tagDao;

    public DeckDAO() {
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
    public boolean isDeckValid(Deck deck) throws SQLException {
        String sql = """
            SELECT deck_id, name
            FROM deck
            WHERE NOT deck_id = '%1$s' AND name = '%2$s'
            """.formatted(deck.getId().toString(),
                deck.getName());

        try (ResultSet res = database.executeQuery(sql)) {
            return !res.next();
        }
    }

    public boolean deckNameExists(String name) throws SQLException {
        String sql = """
            SELECT name
            FROM deck
            WHERE name = '%1$s'
            """.formatted(name);

        try (ResultSet res = database.executeQuery(sql)) {
            return res.next();
        }
    }

    /**
     * Write to long-term memory the contents of a deck
     * <p>
     * If the given deck is not valid, it will be ignored.
     * @see DeckDAO#isDeckValid
     */
    public void saveDeck(Deck deck, UUID userId) throws SQLException {
        if (!isDeckValid(deck))
            return;
        saveDeckIdentity(deck, userId);
        saveDeckTags(deck);
        saveDeckCards(deck);
    }

    /**
     * Update a deck’s name
     * <p>
     * Upon the saving of multiple decks with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    private void saveDeckIdentity(Deck deck, UUID userId)  throws SQLException{
        String sql = """
                INSERT INTO deck (deck_id, user_id, name)
                VALUES ('%1$s', '%2$s', '%3$s')
                ON CONFLICT(deck_id)
                DO UPDATE SET name = '%3$s'
                ON CONFLICT(name)
                DO NOTHING
                """.formatted(
                deck.getId().toString(),
                userId.toString(),
                deck.getName());

        database.executeUpdate(sql);
    }

    private void saveDeckTags(Deck deck) throws SQLException {
        tagDao.saveTagsFor(deck);
    }


    /**
     * Update the database with the data from the deck
     */
    @SuppressWarnings("unchecked")
    private void saveDeckCards(Deck deck) throws SQLException {
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

    private void saveCard(Card card) throws SQLException {
        String sql = """
                INSERT INTO card (card_id, deck_id, front, back)
                VALUES ('%1$s', '%2$s', '%3$s', '%4$s')
                ON CONFLICT(card_id)
                DO UPDATE SET front = '%3$s', back = '%4$s'
                """.formatted(
                card.getId().toString(),
                card.getDeckId().toString(),
                card.getFront(),
                card.getBack());

        database.executeUpdate(sql);
    }

    private void deleteCard(Card card) throws SQLException {
        String sql = """
                DELETE FROM card
                WHERE card.card_id = '%s'
                """.formatted(card.getId().toString());

        database.executeUpdate(sql);
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
    public Deck getDeck(UUID uuid) throws SQLException{
        String sql = """
                SELECT deck_id, name
                FROM deck
                WHERE deck_id = '%s'
                """.formatted(uuid);

        Deck deck;

        try (ResultSet res = database.executeQuery(sql)) {
            if (!res.next())
                return null;

            String name = res.getString("name");
            List<Card> cards = getCardsFor(uuid);
            List<Tag> tags = tagDao.getTagsFor(uuid);
            deck = new Deck(name, uuid, cards, tags);

            return deck;
        }
    }


    /**
     * General method to fetch decks used for exact and approximate search
     *
     * @param sql Search query
     * @return List of decks
     */
    private List<Deck> getDecks(String sql) throws SQLException {
        List<Deck> decks = new ArrayList<>();

        try (ResultSet res = database.executeQuery(sql)) {
            while (res.next()) {
                UUID uuid = UUID.fromString(res.getString("deck_id"));
                decks.add(getDeck(uuid));
            }

            return decks;
        }
    }

    /**
     * Get all decks present in the database
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllDecks() throws SQLException {
        String sql = """
                SELECT deck_id
                FROM deck
                """;

        return getDecks(sql);
    }

    /**
     * Get all decks associated with given user
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllUserDecks(UUID userId) throws SQLException {
        String sql = """
                SELECT deck_id
                FROM deck
                WHERE deck.user_id = '%s'
                """.formatted(userId.toString());

        return getDecks(sql);
    }

    /**
     * Approximate search of decks with given search string
     *
     * @param userSearch query
     * @return List of decks
     */
    public List<Deck> searchDecks(String userSearch) throws SQLException {
        String sql = """
            SELECT deck_id
            FROM deck
            WHERE name LIKE '%s'
            """.formatted(userSearch + "%");

        return getDecks(sql);
    }

    /**
     * Get all cards associated with given deck
     *
     * @return The cards of the deck. If the id
     * is not in the database, an empty list is
     * returned.
     */
    private List<Card> getCardsFor(UUID deckUuid) throws SQLException {
        String sql = """
                SELECT card_id, deck_id, front, back
                FROM card
                WHERE deck_id = '%s'
                """.formatted(deckUuid.toString());

        List<Card> cards = new ArrayList<>();

        try (ResultSet res = database.executeQuery(sql)) {
            while (res.next())
                cards.add(extractCardFromResultSet(res));

            return cards;
        }
    }

    private Card extractCardFromResultSet(ResultSet res) throws SQLException {
        return new Card(
                UUID.fromString(res.getString("card_id")),
                UUID.fromString(res.getString("deck_id")),
                res.getString("front"),
                res.getString("back"));
    }

    /**
     * Delete a deck along with its cards from the database.
     */
    public void deleteDeck(UUID deckId, UUID userId) throws SQLException {
        String sql = """
                DELETE FROM deck
                WHERE deck_id = '%1$s' and user_id = '%2$s'
                """.formatted(deckId.toString(), userId.toString());

        database.executeUpdate(sql);
    }

}
