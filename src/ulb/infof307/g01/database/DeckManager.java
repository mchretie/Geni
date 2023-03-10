package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.util.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DeckManager {

    // Singleton
    private static DeckManager instance;

    private final Database database = Database.singleton();
    private final TagManager tagManager = TagManager.singleton();

    protected DeckManager() {}

    public static DeckManager singleton() {
        if (instance == null)
            instance = new DeckManager();
        return instance;
    }

    /**
     * Write to longterm memory the contents of a deck
     */
    public void saveDeck(Deck deck) {
        saveDeckIdentity(deck);
        saveDeckTags(deck);
        saveDeckCards(deck);
    }

    /**
     * Update a deck’s name
     */
    private void saveDeckIdentity(Deck deck) {
        String sql = """
            INSERT INTO deck (deck_id, name)
            VALUES ('%1$s', '%2$s')
            ON CONFLICT(deck_id)
            DO UPDATE SET name = '%2$s'
            """.formatted(
                    deck.getId().toString(),
                    deck.getName());

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO
    private void saveDeckTags(Deck deck) {
        // deck.getTags().forEach((t) -> tagManager.updateTag(t));
        // deck.getTags().forEach((t) -> tagManager.addTag(deck, t));
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

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCard(Card card) {
        String sql = """
            DELETE FROM card
            WHERE card.card_id = '%s'
            """.formatted(card.getId().toString());

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the deck identified by the given UUID
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
                List<Tag> tags = new ArrayList<Tag>();  // TODO: get tags
                deck = new Deck(name, uuid, cards, tags);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return deck;
    }

    /**
     * Get all decks present in the database
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
                cards.add(getCardFromResultSet(res));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cards;
    }

    private Card getCardFromResultSet(ResultSet res) throws SQLException {
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

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
