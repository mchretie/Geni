package ulb.infof307.g01.gui.httpclient.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.httpclient.HttpClientAPI;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class DeckDAO extends HttpClientAPI {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public List<Deck> getAllDecks()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get("/api/deck/all");

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), Deck[].class);
    }

    /**
     * Search for decks with the given name. If the name is empty, return all.
     * Useful for partial search.
     *
     * <p>
     *   e.g. "deck" will return "deck1", "deck2"... "deck10".
     * </p>
     * @param deckName The name of the deck to search for.
     */
    public List<Deck> searchDecks(String deckName)
            throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllDecks();


        HttpResponse<String> response
                = get("/api/deck/search" + "&name=" + deckName);

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), Deck[].class);
    }

    public void deleteDeck(Deck deck)
            throws IOException, InterruptedException {

        String query =  "&deck_id=" + deck.getId();
        HttpResponse<String> response = delete("/api/deck/delete" + query);

        checkResponseCode(response.statusCode());
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        HttpResponse<String> response
                = post("/api/deck/save", new Gson().toJson(deck));

        checkResponseCode(response.statusCode());
    }
}
