package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class DeckDAO extends HttpDAO {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public boolean deckExists(String deckName) throws IOException, InterruptedException {

        deckName = deckName.replace(" ", "_");
        HttpResponse<String> response = get(ServerPaths.DECK_EXISTS_PATH + "?name=" + deckName);

        checkResponseCode(response.statusCode());

        return Boolean.parseBoolean(response.body());
    }

    public List<Deck> getAllDecks()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get(ServerPaths.GET_ALL_DECKS_PATH);

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
                = get(ServerPaths.SEARCH_DECKS_PATH
                            + "?name="
                            + deckName.replace(" ", "_"));

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), Deck[].class);
    }

    public void deleteDeck(Deck deck)
            throws IOException, InterruptedException {

        String query =  "?deck_id=" + deck.getId();
        HttpResponse<String> response
                = delete(ServerPaths.DELETE_DECK_PATH + query);

        checkResponseCode(response.statusCode());
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        HttpResponse<String> response
                = post(ServerPaths.SAVE_DECK_PATH, new Gson().toJson(deck));

        checkResponseCode(response.statusCode());
    }

    public void uploadImage(File image, String filename)
            throws IOException, InterruptedException {

        HttpResponse<String> response
                = upload(ServerPaths.SAVE_DECK_IMAGE_PATH, image, filename);

        checkResponseCode(response.statusCode());
    }

}
