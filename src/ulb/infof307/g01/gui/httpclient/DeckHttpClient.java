package ulb.infof307.g01.gui.httpclient;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.httpclient.exceptions.ServerRequestFailed;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DeckHttpClient extends HttpClientAPI {

    public Deck getDeck(String deckId) throws IOException, InterruptedException {
        HttpResponse<String> response = get("/api/deck/get");

        if (response.statusCode() != 200)
            throw new ServerRequestFailed("Server request failed: "
                                                    + response.statusCode());

        return new Gson().fromJson(response.body(), Deck.class);
    }

    @SuppressWarnings("unchecked")
    public List<Deck> searchDecks(String deckName) throws IOException, InterruptedException {

        String path = deckName.isEmpty() ? "/api/deck/all" : "/api/deck/search";
        HttpResponse<String> response = get(path);

        if (response.statusCode() != 200)
            throw new ServerRequestFailed("Server request failed: "
                    + response.statusCode());

        return new Gson().fromJson(response.body(), ArrayList.class);
    }

    public void deleteDeck(String deckId) throws IOException, InterruptedException {
        delete("/api/deck/delete/");
    }

    public void saveDeck(Deck deck) throws IOException, InterruptedException {
        post("/api/deck/save", new Gson().toJson(deck));
    }

    public void updateDeck(Deck deck) throws IOException, InterruptedException {
        put("/api/deck/update", new Gson().toJson(deck));
    }
}
