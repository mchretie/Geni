package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.List;

public class DeckManager {

    private static DeckManager dm;

    public static DeckManager singleton(){
        if (dm == null){
            dm = new DeckManager();
        }
        return dm;
    }

    public Deck getDeck(String name){
        return null;
    }

    public List<Deck> getAllDecks() {
        return null;
    }

    public void createDeck(String name){
        return;
    }

    public void addToDeck(Deck deck, List<Card> cards){
        return;
    }

    public void delDeck(Deck deck){
        return;
    }
}
