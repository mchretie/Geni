package ulb.infof307.g01.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Card;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCardManager extends DatabaseUsingTest {

    CardManager cm = CardManager.singleton();
    DeckManager dm = DeckManager.singleton();

    @Override
    @BeforeEach
    void init() throws OpenedDatabaseException, SQLException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Test
    void getCard_CardExists_ReturnCard() throws CardNotExistsException {
        Card card = new Card("Front", "Back");
        cm.updateCard(card);
        assertEquals(cm.getCard(card.getId()), card);
    }

    @Test
    void getCard_CardNotExists_ThrowsException() {
        assertThrows(CardNotExistsException.class,
                () -> cm.getCard(UUID.randomUUID()));
    }

    // FIXME
    // @Test void getCardsFrom_DeckExists_ReturnListCard()
    //   throws DeckNotExistsException
    // {
    //     Deck deck = dm.createDeck("test");
    //     Card card = new Card("Front", "Back");
    //     dm.addToDeck(deck, List.of(card));
    //     assertEquals(1, cm.getCardsFrom(deck.getId()).size());
    //     assertEquals(card, cm.getCardsFrom(deck.getId()).get(0));
    // }

    // FIXME
    // @Test void getCardsFrom_DeckNotExists_ThrowsException()
    // {
    //     assertThrows(DeckNotExistsException.class, () -> cm.getCardsFrom(new
    //     Deck("test").getId()));
    // }

    @Test
    void delCard_CardExists_CardNotInDB() {
        Card card = new Card("Front", "Back");
        cm.updateCard(card);
        cm.delCard(card);
        assertThrows(CardNotExistsException.class, () -> cm.getCard(card.getId()));
    }

    // FIXME
    // @Test void delCard_CardNotExists_ThrowsException(){
    //     assertThrows(CardNotExistsException.class, () -> cm.delCard(new
    //     Card("Front", "Back")));
    // }

    @Test
    void updateCard_CardExists_CardUpdatedInDB() throws CardNotExistsException {
        Card card = new Card("Front", "Back");
        cm.updateCard(card);
        card.setFront("Front2");
        card.setBack("Back2");
        cm.updateCard(card);
        assertEquals(cm.getCard(card.getId()), card);
    }

    // FIXME
    // @Test void updateCard_CardNotExists_ThrowsException()
    //   throws DatabaseNotInitException
    // {
    //     assertThrows(CardNotExistsException.class, () -> cm.updateCard(new
    //     Card("Front", "Back")));
    // }
}
