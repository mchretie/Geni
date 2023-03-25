package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.httpclient.exceptions.ServerRequestFailed;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class DeckDAO extends HttpClientAPI {

    private String user;


    /* ====================================================================== */
    /*                           Singleton pattern                            */
    /* ====================================================================== */

    private static DeckDAO instance;

    private DeckDAO() {
        super();
    }

    public static DeckDAO getInstance() {
        if (instance == null)
            instance = new DeckDAO();
        return instance;
    }


    /* ====================================================================== */
    /*                                  Setter                                */
    /* ====================================================================== */

    /**
     * Set the user id to the query string. To be called before any DAO method.
     */
    public void setUser(UUID user) {
        this.user = "?userid=" + user;
    }

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public List<Deck> getAllDecks()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get("/api/deck/all" + user);

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), Deck[].class);
    }

    public List<Deck> searchDecks(String deckName) throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllDecks();

        String query = user + "&name=" + deckName;
        HttpResponse<String> response = get("/api/deck/search" + query);

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), Deck[].class);
    }

    public void deleteDeck(Deck deck)
            throws IOException, InterruptedException {

        String query = user + "&id=" + deck.getId();

        delete("/api/deck/delete" + query);
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        post("/api/deck/save" + user, new Gson().toJson(deck));
    }
}
