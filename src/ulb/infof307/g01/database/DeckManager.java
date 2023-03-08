package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeckManager {

    private static DeckManager dm;

    public static DeckManager singleton() {
        if (dm == null) {
            dm = new DeckManager();
        }
        return dm;
    }

    boolean deckNotExists(UUID uuid) throws DatabaseNotInitException {
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT count(*) FROM deck WHERE deck_id = " + '"' + uuid + '"');
            if (response.next()) {
                return response.getInt("count(*)") == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Deck getDeck(UUID uuid) throws DeckNotExistsException, DatabaseNotInitException {
        if (deckNotExists(uuid)) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, deck_id FROM deck WHERE deck_id = " + '"' + uuid + '"');
            if (response.next()) {
                List<Card> cards = CardManager.singleton().getCardsFrom(uuid);
                List<Tag> tags = TagManager.singleton().getTagsFor(uuid);
                return new Deck(response.getString("name"), UUID.fromString(response.getString("deck_id")), cards, tags);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Deck> getAllDecks() throws DeckNotExistsException, DatabaseNotInitException {
        List<Deck> decks = new ArrayList<>();
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, deck_id FROM deck");
            while (response.next()) {
                UUID uuid = UUID.fromString(response.getString("deck_id"));
                List<Card> cards = CardManager.singleton().getCardsFrom(uuid);
                List<Tag> tags = TagManager.singleton().getTagsFor(uuid);
                decks.add(new Deck(response.getString("name"), uuid, cards, tags));
            }
        } catch (SQLException e) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
        return decks;
    }

    public Deck createDeck(String name) throws DatabaseNotInitException {
        try {
            Deck deck = new Deck(name);
            Database.singleton().executeUpdate("INSERT INTO deck (name, deck_id) VALUES ('" + deck.getName() + "', '" + deck.getId() + "')");
            return deck;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToDeck(Deck deck, List<Card> cards) throws DeckNotExistsException, DatabaseNotInitException {
        deck.addCards(cards);
    }

    public void delDeck(Deck deck) throws DatabaseNotInitException, DeckNotExistsException {
        if (deckNotExists(deck.getId())) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
        try {
            Database.singleton().executeUpdate("DELETE FROM deck_tag WHERE deck_id = " + '"' + deck.getId() + '"');
            Database.singleton().executeUpdate("DELETE FROM deck_card WHERE deck_id = " + '"' + deck.getId() + '"');
            Database.singleton().executeUpdate("DELETE FROM deck WHERE deck_id = " + '"' + deck.getId() + '"');
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
