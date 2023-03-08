package ulb.infof307.g01.database;

import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCardManager {
    CardManager cm = CardManager.singleton();
    DeckManager dm = DeckManager.singleton();
    @Test void getCard_CardExists_ReturnCard(){
        Card card = new Card("Front", "Back");
        cm.addCard(card);
        assertEquals(cm.getCard(card.getId()), card);
        cm.delCard(card);
    }

    @Test void getCard_CardNotExists_ThrowsException(){
        assertThrows(CardNotExistsException.class, () -> cm.getCard(new Card("Front", "Back").getId()));
    }

    @Test void getCardsFrom_DeckExists_ReturnListCard() throws DatabaseNotInitException, DeckNotExistsException {
        Deck deck = dm.createDeck("test");
        Card card = new Card("Front", "Back");
        dm.addToDeck(deck, List.of(card));
        assertEquals(cm.getCardsFrom(deck.getId()).size(), 1);
        assertEquals(cm.getCardsFrom(deck.getId()).get(0), card);
        dm.delDeck(deck);
    }

    @Test void getCardsFrom_DeckNotExists_ThrowsException(){
        assertThrows(DeckNotExistsException.class, () -> cm.getCardsFrom(new Deck("test").getId()));
    }

//    @Test void addCard_CardUnique_CardInDB(){
//
//    }
//
//    @Test void addCard_CardNotUnique_ThrowsException(){
//
//    }

    @Test void delCard_CardExists_CardNotInDB(){
        Card card = new Card("Front", "Back");
        cm.addCard(card);
        cm.delCard(card);
        assertThrows(CardNotExistsException.class, () -> cm.getCard(card.getId()));
    }

    @Test void delCard_CardNotExists_ThrowsException(){
        assertThrows(CardNotExistsException.class, () -> cm.delCard(new Card("Front", "Back")));
    }

    @Test void updateCard_CardExists_CardUpdatedInDB(){
        Card card = new Card("Front", "Back");
        cm.addCard(card);
        card.setFront("Front2");
        card.setBack("Back2");
        cm.updateCard(card);
        assertEquals(cm.getCard(card.getId()), card);
        cm.delCard(card);
    }

    @Test void updateCard_CardNotExists_ThrowsException(){
        assertThrows(CardNotExistsException.class, () -> cm.updateCard(new Card("Front", "Back")));
    }
}
