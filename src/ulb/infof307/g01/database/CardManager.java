package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Card;

import java.util.List;
import java.util.UUID;

public class CardManager {

    private static CardManager cm;

    public static CardManager singleton(){
        if (cm == null){
            cm = new CardManager();
        }
        return cm;
    }
    public Card getCard(UUID uuid){
        return null;
    }

    public List<Card> getCardsFrom(UUID deckUuid){
        return null;
    }
    public void addCard(Card card){
        return;
    }

    public void delCard(Card card){
        return;
    }

    public void updateCard(Card card){
        return;
    }
}
