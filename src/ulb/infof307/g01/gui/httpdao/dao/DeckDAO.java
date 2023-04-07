package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.Deck;

import ulb.infof307.g01.model.DeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;

public class DeckDAO extends HttpDAO {

    Map<UUID, Deck> cachedDecks = new HashMap<>();
    Map<UUID, DeckMetadata> deckMetadata = new HashMap<>();
    Optional<HashSet<UUID>> allDecksIds = Optional.empty();

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public boolean deckExists(String deckName) throws IOException, InterruptedException {
        if (allDecksIds.isEmpty()) {
            // Fill the complete list of decks
            fetchAllDecksMetadata();
        }

        for (UUID deckId: allDecksIds.get()) {
            assert deckMetadata.containsKey(deckId);

            if (deckMetadata.get(deckId).name().equals(deckName)) {
                return true;
            }
        }

        return false;
        // deckName = deckName.replace(" ", "_");
        // HttpResponse<String> response = get(ServerPaths.DECK_EXISTS_PATH + "?name=" + deckName);

        // checkResponseCode(response.statusCode());

        // return Boolean.parseBoolean(response.body());
    }

    /**
     * Fill the cached data related to decks metadata
     * @throws IOException
     * @throws InterruptedException
     */
    private void fetchAllDecksMetadata()
            throws IOException, InterruptedException {

        HttpResponse<String> response = get(ServerPaths.GET_ALL_DECKS_PATH);
        checkResponseCode(response.statusCode());

        allDecksIds = Optional.of(new HashSet<UUID>());
        Set<UUID> allDecksIdsSet = allDecksIds.get();

        List<DeckMetadata> decks =  stringToArray(response.body(), DeckMetadata[].class);
        for (DeckMetadata deck: decks) {
            allDecksIdsSet.add(deck.id());
            deckMetadata.put(deck.id(), deck);
        }
    }

    public List<DeckMetadata> getAllDecks()
            throws IOException, InterruptedException {

        if (allDecksIds.isEmpty()) {
            fetchAllDecksMetadata();
        }

        List<DeckMetadata> decks = new ArrayList<>();
        for (UUID deckId: allDecksIds.get()) {
            decks.add(new DeckMetadata(deckMetadata.get(deckId)));
        }

        return decks;

//        HttpResponse<String> response = get(ServerPaths.GET_ALL_DECKS_PATH);
//
//        checkResponseCode(response.statusCode());
//
//        return stringToArray(response.body(), DeckMetadata[].class);
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
    public List<DeckMetadata> searchDecks(String deckName)
            throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllDecks();

        HttpResponse<String> response
                = get(ServerPaths.SEARCH_DECKS_PATH
                            + "?name="
                            + deckName.replace(" ", "_"));

        checkResponseCode(response.statusCode());

        return stringToArray(response.body(), DeckMetadata[].class);
    }

    public void deleteDeck(DeckMetadata deck)
            throws IOException, InterruptedException {

        String query =  "?deck_id=" + deck.id();
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

    public Deck getDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {

        String path = ServerPaths.GET_DECK_PATH;
        String parameters = "?deck_id=%s".formatted(deckMetadata.id());
        HttpResponse<String> response = get(path + parameters);

        return new Gson().fromJson(response.body(), Deck.class);
    }
}
