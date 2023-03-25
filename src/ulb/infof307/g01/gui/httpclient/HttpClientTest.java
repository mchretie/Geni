package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;
import ulb.infof307.g01.model.Deck;

import java.util.Arrays;
import java.util.List;


public class HttpClientTest {

    private static void httpClientTest() {
        DeckHttpClient deckHttpClient = new DeckHttpClient();
        try {
            List<Deck> decks = deckHttpClient.searchDecks("");

            decks.forEach(deck -> {
                System.out.println(deck.getName());
                deck.getCards().forEach(card -> {
                    System.out.println(" - " + card.getFront());
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    private static void gsonTest() {
        String input = "[{\"name\":\"Deck 1\",\"id\":\"fd7e65db-f2f0-452a-b27a-a8777fdda2fd\",\"cards\":[{\"id\":\"82086478-7a90-47f9-bd9c-4aac9b8dbe0d\",\"deckId\":\"fd7e65db-f2f0-452a-b27a-a8777fdda2fd\",\"front\":\"Card 1\",\"back\":\"Definition 1\",\"knowledge\":\"NEVER_SEEN\"}],\"tags\":[]},{\"name\":\"Deck 2\",\"id\":\"134840ad-6ef7-4057-82a8-cca91a0278e2\",\"cards\":[],\"tags\":[]},{\"name\":\"Deck 3\",\"id\":\"38aacd32-d7e6-4550-8da7-74ed53df8f39\",\"cards\":[],\"tags\":[]}]";
        List<Deck> decks = stringToArray(input, Deck[].class);
        decks.forEach(d -> System.out.println(d.getName()));
    }

    public static void main(String[] args) {
        httpClientTest();
    }
}
