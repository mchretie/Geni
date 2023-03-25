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

    private static DeckDAO instance;

    private DeckDAO() {
        super();
    }

    public static DeckDAO getInstance() {
        if (instance == null)
            instance = new DeckDAO();
        return instance;
    }

    public void setUser(UUID user) {
        this.user = "?user=" + user;
    }

    public List<Deck> getAllDecks()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get("/api/deck/all" + user);

        if (response.statusCode() != 200)
            throw new ServerRequestFailed("Server request failed: "
                    + response.statusCode());

        return stringToArray(reformatString(response.body()), Deck[].class);
    }

    public List<Deck> searchDecks(String deckName)
            throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllDecks();

        String path = "/api/deck/search";
        String query = user + "&name=" + deckName;

        HttpResponse<String> response = get(path + query);

        if (response.statusCode() != 200)
            throw new ServerRequestFailed("Server request failed: "
                    + response.statusCode());

        return stringToArray(reformatString(response.body()), Deck[].class);
    }

    public void deleteDeck(Deck deck)
            throws IOException, InterruptedException {

        String path = "/api/deck/delete";
        String query = user + "&id=" + deck.getId();

        delete(path + query);
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        String path = "/api/deck/save";

        post("/api/deck/save" + user, new Gson().toJson(deck));
    }
}
