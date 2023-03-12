package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Save and retrieve decks from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Deck, apply changes
 * on it before commiting them to long-term
 * with saveDeck or deleteDeck.
 */
public class DeckManager {

    // Singleton
    private static DeckManager instance;

    private final static Database database = Database.singleton();
    private final static TagManager tagManager = TagManager.singleton();

    protected DeckManager() {
    }

    public static DeckManager singleton() {
        if (instance == null)
            instance = new DeckManager();
        return instance;
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
     * @see ulb.infof307.g01.database.DeckManager.deckNameExists
     */
    public boolean isDeckValid(Deck deck) {
        String sql = """
            SELECT deck_id, name
            FROM deck
            WHERE NOT deck_id = '%1$s' AND name = '%2$s'
            """.formatted(deck.getId().toString(),
                          deck.getName());

        try {
            return !database.executeQuery(sql).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deckNameExists(String name) {
        String sql = """
            SELECT name
            FROM deck
            WHERE name = '%1$s'
            """.formatted(name);

        try {
            return database.executeQuery(sql).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write to longterm memory the contents of a deck
     * <p>
     * If the given deck is not valid, it will be ignored.
     * @see ulb.infof307.g01.database.DeckManager.isDeckValid
     */
    public void saveDeck(Deck deck) {
        if (!isDeckValid(deck))
            return;
        saveDeckIdentity(deck);
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
    private void saveDeckIdentity(Deck deck) {
        String sql = """
                INSERT INTO deck (deck_id, name)
                VALUES ('%1$s', '%2$s')
                ON CONFLICT(deck_id)
                DO UPDATE SET name = '%2$s'
                ON CONFLICT(name)
                DO NOTHING
                """.formatted(
                deck.getId().toString(),
                deck.getName());

        database.executeUpdate(sql);
    }

    private void saveDeckTags(Deck deck) {
        tagManager.saveTagsFor(deck);
    }

    /**
     * Update the database with the data from the deck
     */
    private void saveDeckCards(Deck deck) {
        HashSet<Card> currentCards = new HashSet<Card>(getCardsFor(deck.getId()));
        HashSet<Card> newCards = new HashSet<Card>(deck.getCards());

        Set<Card> addedCards = (Set<Card>) newCards.clone();
        addedCards.removeAll(currentCards);

        Set<Card> deletedCards = (Set<Card>) currentCards.clone();
        deletedCards.removeAll(newCards);

        addedCards.forEach((c) -> saveCard(c));
        deletedCards.forEach((c) -> deleteCard(c));
    }

    private void saveCard(Card card) {
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

    private void deleteCard(Card card) {
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
     * To retieve decks from memory, use getAllDecks
     * or other managers returning decks.
     * <p>
     *
     * @return The deck requested or null if it doesn’t exist.
     */
    public Deck getDeck(UUID uuid) {
        String sql = """
                SELECT deck_id, name
                FROM deck
                WHERE deck_id = '%s'
                """.formatted(uuid);

        Deck deck = null;

        try {
            ResultSet res = database.executeQuery(sql);
            if (res.next()) {
                String name = res.getString("name");
                List<Card> cards = getCardsFor(uuid);
                List<Tag> tags = tagManager.getTagsFor(uuid);
                deck = new Deck(name, uuid, cards, tags);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return deck;
    }

    /**
     * Get all decks present in the database
     *
     * @return A list of all decks, empty if none are saved.
     */
    public List<Deck> getAllDecks() {
        String sql = """
                SELECT deck_id
                FROM deck
                """;

        List<Deck> decks = new ArrayList<>();

        try {
            ResultSet res = database.executeQuery(sql);
            while (res.next()) {
                UUID deckId = UUID.fromString(res.getString("deck_id"));
                decks.add(getDeck(deckId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return decks;
    }

    /**
     * Get all cards associated with given deck
     *
     * @return The cards of the deck. If the id
     * is not in the database, an empty list is
     * returned.
     */
    private List<Card> getCardsFor(UUID deckUuid) {
        String sql = """
                SELECT card_id, deck_id, front, back
                FROM card
                WHERE deck_id = '%s'
                """.formatted(deckUuid.toString());

        List<Card> cards = new ArrayList<Card>();

        try {
            ResultSet res = database.executeQuery(sql);
            while (res.next()) {
                cards.add(extractCardFromResultSet(res));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cards;
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
    public void deleteDeck(Deck deck) {
        String sql = """
                DELETE FROM deck
                WHERE deck_id = '%s'
                """.formatted(deck.getId());

        database.executeUpdate(sql);
    }
}
