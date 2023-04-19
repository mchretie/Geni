package ulb.infof307.g01;

import com.google.gson.Gson;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.Deck;

public class Scratch
{
    public static void main(String[] args)
    {
        Deck deck1 = new Deck("name");
        Card card1 = new FlashCard();
        Card card2 = new MCQCard();
        Card card3 = new InputCard();

        deck1.addCard(card1);
        deck1.addCard(card2);
        deck1.addCard(card3);

        String deck1Gson = new Gson().toJson(deck1);
        System.out.println(deck1Gson);
    }
}
