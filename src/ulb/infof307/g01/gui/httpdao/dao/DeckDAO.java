package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ulb.infof307.g01.gui.util.DeckCache;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.DeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * Access to decks from a HTTP server
 * <p>
 * The methods returning multiple decks return all metadata since
 * most of the time, at most one deck will be open at a given moment.
 * Consequently, the proper deck should be fetched only when needed.
 * This is for increased performances.
 * </p>
 */
public class DeckDAO extends HttpDAO {

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

    /* ====================================================================== */
    /*                          Fetching methods                              */
    /* ====================================================================== */

    private List<DeckMetadata> fetchAllDecksMetadata()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get(ServerPaths.GET_ALL_DECKS_PATH);
        checkResponseCode(response.statusCode());

        return stringToDeckArray(response.body());
    }

    private Deck fetchDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {

        String path = ServerPaths.GET_DECK_PATH;
        String parameters = "?deck_id=%s".formatted(deckMetadata.id());
        HttpResponse<String> response = get(path + parameters);

        return new Deck(new Gson().fromJson(response.body(), JsonObject.class));
    }

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public boolean deckExists(String deckName)
            throws IOException, InterruptedException {

        initCacheIfNot();
        return deckCache.getAllDecksMetadata().stream()
                .anyMatch(deck -> deck.name().equals(deckName));
    }


    public List<DeckMetadata> getAllDecksMetadata()
            throws IOException, InterruptedException {

        initCacheIfNot();
        return deckCache.getAllDecksMetadata();
    }

    /**
     * Search for decks with the given name
     *
     * <p>
     * If the name is empty, return all. Useful for partial search.
     * The returned decks will match this regex: "name.*".
     * <p>
     * E.g. "deck" will return "deck1", "deck2"... "deck10".
     * </p>
     *
     * @param deckName The name of the deck to search for.
     */
    public List<DeckMetadata> searchDecks(String deckName)
            throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllDecksMetadata();

        final Pattern pattern = Pattern.compile("%s.*".formatted(deckName));
        return getAllDecksMetadata().stream()
                .filter(deck -> pattern.matcher(deck.name()).matches())
                .collect(toList());
    }

    public List<DeckMetadata> searchDecksByTags(String name)
            throws IOException, InterruptedException {
        if (name.isEmpty())
            return getAllDecksMetadata();

        final Pattern pattern = Pattern.compile("%s.*".formatted(name));
        return getAllDecksMetadata().stream()
                .filter(deck -> deck.tags().stream()
                        .anyMatch(tag -> pattern.matcher(tag.getName()).matches()))
                .collect(toList());
    }

    public void deleteDeck(DeckMetadata deck)
            throws IOException, InterruptedException {

        String query = "?deck_id=" + deck.id();
        String path = ServerPaths.DELETE_DECK_PATH;
        HttpResponse<String> response = delete(path + query);

        checkResponseCode(response.statusCode());

        deckCache.removeDeck(deck);
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        String path = ServerPaths.SAVE_DECK_PATH;
        HttpResponse<String> response = post(path, new Gson().toJson(deck));

        checkResponseCode(response.statusCode());

        deckCache.updateDeck(deck);
    }

    public Optional<Deck> getDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {

        if (deckCache.getDeck(deckMetadata).isEmpty())
            deckCache.updateDeck(fetchDeck(deckMetadata));
        return deckCache.getDeck(deckMetadata);
    }

    @Override
    public void setToken(String token) {
        deckCache = null;
        super.setToken(token);
    }

}
