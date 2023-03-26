package ulb.infof307.g01.gui.httpclient;

import ulb.infof307.g01.model.Deck;

import java.util.List;
import java.util.UUID;

/**
 * This class is used to test the HttpClientAPI class.
 *  Will be removed later.
 */
public class HttpClientVerifier {

    private static void getAllDecksVerifier() {
        try {
            DeckDAO deckHttpClient = DeckDAO.getInstance();
            deckHttpClient.setUser(UUID.randomUUID());

            List<Deck> decks = deckHttpClient.getAllDecks();

            decks.forEach(deck -> {
                System.out.println(deck.getName());
                deck.getCards().forEach(card -> System.out.println(
                        " - " + card.getFront() + " : " + card.getBack()));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // getAllDecksVerifier();
        System.out.println(UUID.randomUUID());
    }
}
