package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.database.DeckNotExistsException;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeckManager {

    private static DeckManager dm;

    public static DeckManager singleton(){
        if (dm == null){
            dm = new DeckManager();
        }
        return dm;
    }

    public Deck getDeck(UUID uuid) throws DeckNotExistsException, DatabaseNotInitException {
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, deck_id FROM deck WHERE deck_id = " + uuid);
            if (response.next()) {
                List<Card> cards = CardManager.singleton().getCardsFrom(uuid);
                List<Tag> tags = TagManager.singleton().getTagsFor(uuid);
                return new Deck(response.getString("name"), UUID.fromString(response.getString("deck_id")), cards, tags);
            }
        } catch (SQLException e) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
        return null;
    }

    public List<Deck> getAllDecks() throws DeckNotExistsException, DatabaseNotInitException {
        List<Deck> decks = new ArrayList<>();
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, deck_id FROM deck");
            while (response.next()) {
                decks.add(new Deck(response.getString("name"),  UUID.fromString(response.getString("deck_id"))));
            }
        } catch (SQLException e) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
        return decks;
    }

    public Deck createDeck(String name) throws DatabaseNotInitException{
        try {
            Deck deck = new Deck(name);
            Database.singleton().executeUpdate("INSERT INTO deck (name, deck_id) VALUES ('" + deck.getName() + "', '" + deck.getId() + "')");
            return deck;
        } catch (SQLException e) {
            throw new RuntimeException("Deck already in DB");
        }
    }

    public void addToDeck(Deck deck, List<Card> cards){
        deck.addCards(cards);
    }

    public void delDeck(Deck deck) throws DatabaseNotInitException, DeckNotExistsException {
        try {
            Database.singleton().executeUpdate("DELETE FROM deck WHERE deck_id = " + deck.getId());
        } catch (SQLException e) {
            throw new DeckNotExistsException("Could not find requested deck");
        }
    }
}
