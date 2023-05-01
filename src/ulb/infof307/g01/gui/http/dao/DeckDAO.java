package ulb.infof307.g01.gui.http.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.util.DeckCache;
import ulb.infof307.g01.model.IndulgentValidator;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * Access to decks from an HTTP server
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
    IndulgentValidator validator = new IndulgentValidator();

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
        return Deck.fromJson(response.body());
    }

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public boolean deckExists(String deckName)
            throws IOException, InterruptedException {
        HttpResponse<String> response = post(ServerPaths.DECK_EXISTS_PATH, deckName);
        checkResponseCode(response.statusCode());
        return Boolean.parseBoolean(response.body());
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

        final Pattern pattern = Pattern.compile("%s.*".formatted(validator.addTolerance(deckName)));

        return getAllDecksMetadata().stream()
                .filter(deck -> pattern.matcher(validator.addTolerance(deck.name())).matches())
                .collect(toList());
    }

    public List<DeckMetadata> searchDecksByTags(String tagName)
            throws IOException, InterruptedException {
        if (tagName.isEmpty())
            return getAllDecksMetadata();

        final Pattern pattern = Pattern.compile("%s.*".formatted(validator.addTolerance(tagName)));

        return getAllDecksMetadata().stream()
                .filter(deck -> deck.tags().stream()
                        .anyMatch(tag -> pattern.matcher(validator.addTolerance(tag.getName())).matches()))
                .collect(toList());
    }

    public void deleteDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {
        String path = ServerPaths.DELETE_DECK_PATH + "?deck_id=" + deckMetadata.id().toString();

        HttpResponse<String> response = delete(path);

        checkResponseCode(response.statusCode());
        deckCache.removeDeck(deckMetadata);
    }

    public void removeDeckFromCollection(DeckMetadata deckId)
            throws IOException, InterruptedException {

        String query = "?deck_id=" + deckId.id().toString();
        String path = ServerPaths.REMOVE_DECK_FROM_COLLECTION_PATH;

        HttpResponse<String> response = delete(path + query);

        checkResponseCode(response.statusCode());
        deckCache.removeDeck(deckId);
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        String path = ServerPaths.SAVE_DECK_PATH;
        HttpResponse<String> response = post(path, new Gson().toJson(deck));
        checkResponseCode(response.statusCode());

        deckCache.updateDeck(fetchDeck(deck.getMetadata()));
    }

    public Optional<Deck> getDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {

        if (deckCache.getDeck(deckMetadata).isEmpty())
            deckCache.updateDeck(fetchDeck(deckMetadata));
        return deckCache.getDeck(deckMetadata);
    }

    public int getDeckCount() throws IOException, InterruptedException {
        initCacheIfNot();
        return deckCache.getAllDecksMetadata().size();
    }

    public void uploadImage(File image, String filename)
            throws IOException, InterruptedException {

        HttpResponse<String> response
                = upload(image, filename);

        checkResponseCode(response.statusCode());
    }

    public void addDeckToCollection(DeckMetadata deckMetadata) throws IOException, InterruptedException {
        String path = ServerPaths.ADD_DECK_TO_COLLECTION_PATH + "?deck_id=" + deckMetadata.id();
        HttpResponse<String> response = post(path, "");

        checkResponseCode(response.statusCode());

        deckCache.updateDeckMetadata(deckMetadata);
    }

    public int numberOfPublicPlayedDecks() throws IOException, InterruptedException {
        String path = ServerPaths.NUMBER_OF_PUBLIC_PLAYED_DECKS_PATH;
        HttpResponse<String> response = get(path);

        checkResponseCode(response.statusCode());

        return Integer.parseInt(response.body());
    }

    public void updateCache(DeckMetadata deckMetadata) {
        deckCache.updateDeckMetadata(deckMetadata);
    }

    @Override
    public void setJWT(String token) {
        deckCache = null;
        super.setJWT(token);
    }
}
