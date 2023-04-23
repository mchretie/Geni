package ulb.infof307.g01.gui.httpdao.dao;

import ulb.infof307.g01.gui.util.DeckCache;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class MarketplaceDAO extends HttpDAO {
    /* ====================================================================== */
    /*                               Cache                                    */
    /* ====================================================================== */

    DeckCache deckCache = null;

    /**
     * Init and set up the cache with user’s deck collection
     *
     * @throws IOException          Error during the fetching of decks
     * @throws InterruptedException Error with the server
     */
    private void initCacheIfNot() throws IOException, InterruptedException {
        if (deckCache == null) {
            deckCache = new DeckCache(fetchAllDecksMetadata());
        }
    }

    private List<DeckMetadata> fetchAllDecksMetadata()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get(ServerPaths.GET_ALL_DECKS_PATH);  //TODO change path
        checkResponseCode(response.statusCode());

        return stringToDeckArray(response.body());
    }

    /* ====================================================================== */
    /*                          Fetching methods                              */
    /* ====================================================================== */
}
