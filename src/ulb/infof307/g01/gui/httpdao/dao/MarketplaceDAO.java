package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import ulb.infof307.g01.gui.util.DeckCache;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceDAO extends HttpDAO {

    /* ====================================================================== */
    /*                          Fetching methods                              */
    /* ====================================================================== */

    public List<MarketplaceDeckMetadata> getAllMarketplaceDecks() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_MARKETPLACE_PATH);

        checkResponseCode(response.statusCode());

        List<MarketplaceDeckMetadata> marketplaceDeckMetadataList = new ArrayList<>();
        JsonArray jsonArray = new Gson().fromJson(response.body(), JsonArray.class);
        for (int i = 0; i < jsonArray.size(); i++) {
            marketplaceDeckMetadataList.add(new Gson().fromJson(response.body(), MarketplaceDeckMetadata.class));
        }

        return marketplaceDeckMetadataList;
    }

    public void addDeckToMarketplace(String deckId) throws IOException, InterruptedException {
        String deckIdParam = "?deckId=" + deckId;

        HttpResponse<String> response = post(ServerPaths.ADD_DECK_TO_MARKETPLACE_PATH + deckIdParam, "");

        checkResponseCode(response.statusCode());
    }

    public void removeDeckFromMarketplace(String deckId) throws IOException, InterruptedException {
        String deckIdParam = "?deckId=" + deckId;

        HttpResponse<String> response = delete(ServerPaths.REMOVE_DECK_FROM_MARKETPLACE_PATH + deckIdParam);

        checkResponseCode(response.statusCode());
    }

    public void addDeckToCollection(String deckId) throws IOException, InterruptedException {
        String deckIdParam = "?deckId=" + deckId;

        HttpResponse<String> response = post(ServerPaths.ADD_DECK_TO_COLLECTION_PATH + deckIdParam, "");

        checkResponseCode(response.statusCode());
    }

    public void removeDeckFromCollection(String deckId) throws IOException, InterruptedException {
        String deckIdParam = "?deckId=" + deckId;

        HttpResponse<String> response = delete(ServerPaths.REMOVE_DECK_FROM_COLLECTION_PATH + deckIdParam);

        checkResponseCode(response.statusCode());
    }
}
