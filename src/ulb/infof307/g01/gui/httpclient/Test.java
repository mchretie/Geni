package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.ArrayList;
import java.util.List;


public class Test {
    public static void main(String[] args) {
        DeckHttpClient deckHttpClient = new DeckHttpClient();
        try {
            List<Deck> decks = deckHttpClient.getAllDecks();
            System.out.println(decks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
