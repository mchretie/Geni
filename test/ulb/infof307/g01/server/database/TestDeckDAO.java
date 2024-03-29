package ulb.infof307.g01.server.database;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Tag;
import ulb.infof307.g01.server.database.dao.CardDAO;
import ulb.infof307.g01.server.database.dao.DeckDAO;
import ulb.infof307.g01.server.database.dao.TagDAO;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeckDAO extends DatabaseUsingTest {

    DeckDAO deckDAO;
    CardDAO cardDAO;
    TagDAO tagDAO;
    UserDAO userDAO;

    UUID user;

    @Override
    @BeforeEach
    void init() throws DatabaseException {
        super.init();

        db.initTables(DatabaseSchema.SERVER);

        this.deckDAO = new DeckDAO(this.db);
        this.cardDAO = new CardDAO(this.db);
        this.tagDAO = new TagDAO(this.db);
        this.userDAO = new UserDAO(this.db);

        this.deckDAO.setTagDao(this.tagDAO);
        this.deckDAO.setCardDao(this.cardDAO);
        this.tagDAO.setDeckDao(this.deckDAO);

        userDAO.registerUser("user", "pass");
        this.user = UUID.fromString(userDAO.getUserId("user"));
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() throws DatabaseException {
        assertNull(deckDAO.getDeck(new Deck("name").getId()));
    }

    @Test
    void deckNameExists_NameExists_ReturnsTrue() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        assertTrue(deckDAO.deckNameExists("name", user));
    }

    @Test
    void isDeckValid_DeckInvalid_ReturnsFalse() throws DatabaseException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");
        deckDAO.saveDeck(deck1, user);

        assertFalse(deckDAO.isDeckValid(deck2, user));
    }

    @Test
    void isDeckValid_DeckValid_ReturnsTrue() throws DatabaseException {
        Deck deck = new Deck("name");
        assertTrue(deckDAO.isDeckValid(deck, user));

        deckDAO.saveDeck(deck, user);
        assertTrue(deckDAO.isDeckValid(deck, user));
    }

    @Test
    void saveDeck_DecksWithSameNameDiffId_OnlyFirstAdded() throws DatabaseException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");

        deckDAO.saveDeck(deck1, user);
        deckDAO.saveDeck(deck2, user);

        assertEquals(Set.of(deck1), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        deck.setName("name_01");
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        deck.setName("name_01");

        assertNotEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() throws DatabaseException {
        Deck deck = new Deck("name");
        Card card = new FlashCard();
        Card card2 = new MCQCard();
        Card card3 = new InputCard();

        deck.addCard(card);
        deck.addCard(card2);
        deck.addCard(card3);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() throws DatabaseException {
        Deck deck = new Deck("name");
        Card card = new FlashCard();
        Card card2 = new MCQCard();
        Card card3 = new InputCard();

        deck.addCard(card);
        deck.addCard(card2);
        deck.addCard(card3);
        deckDAO.saveDeck(deck, user);

        deck.removeCard(card);
        deck.removeCard(card2);
        deck.removeCard(card3);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagAdded_DeckUpdated() throws DatabaseException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagRemoved_DeckUpdated() throws DatabaseException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, user);
        deck.removeTag(tag);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDecks_SameNameTagsAdded_DeckUpdated() throws DatabaseException {
        Deck deck1 = new Deck("deck1");
        Tag tag1 = new Tag("tag");
        deck1.addTag(tag1);
        deckDAO.saveDeck(deck1, user);

        Deck deck2 = new Deck("deck2");
        Tag tag2 = new Tag("tag");
        deck2.addTag(tag2);
        deckDAO.saveDeck(deck2, user);

        Deck updatedDeck1 = deckDAO.getDeck(deck1.getId());
        Deck updatedDeck2 = deckDAO.getDeck(deck2.getId());

        assertEquals(updatedDeck1.getTags().get(0).getId(), updatedDeck2.getTags().get(0).getId());
    }

    @Test
    void getAllDecks_NoDecks_EmptyList() throws DatabaseException {
        assertTrue(deckDAO.getAllDecks().isEmpty());
    }

    @Test
    void getAllDecks_ManyDecks_AllReturned() throws DatabaseException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d, user);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);
        deckDAO.saveDeck(deck, user);
        deckDAO.saveDeck(deck, user);
        assertEquals(Set.of(deck), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() throws DatabaseException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);
        deckDAO.deleteDeck(deck.getId(), user);

        assertNull(deckDAO.getDeck(deck.getId()));
    }

    @Test
    void deleteDeck_DeckNotExists_NoThrow() {
        Deck deck = new Deck("name");
        assertDoesNotThrow(() -> deckDAO.deleteDeck(deck.getId(), user));
    }

    @Test
    void searchTags() throws DatabaseException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d, user);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.searchDecks("name", user)));
    }

    @Test
    void gsonDeckConversion_DeckConverted_DecksEqual() {
        Deck deck1 = new Deck("name");
        Card card1 = new FlashCard();
        Card card2 = new MCQCard();
        Card card3 = new InputCard();

        deck1.addCard(card1);
        deck1.addCard(card2);
        deck1.addCard(card3);

        String deck1Gson = new Gson().toJson(deck1);
        Deck deck2 = Deck.fromJson(deck1Gson);
        String deck2Gson = new Gson().toJson(deck2);

        assertEquals(deck1Gson, deck2Gson);
    }
}
