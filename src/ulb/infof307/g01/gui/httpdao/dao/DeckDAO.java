package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import ulb.infof307.g01.model.Deck;

import ulb.infof307.g01.model.DeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Pattern;

public class DeckDAO extends HttpDAO {

    Map<UUID, Deck> cachedDecks = new HashMap<>();
    Map<UUID, DeckMetadata> deckMetadata = new HashMap<>();
    HashSet<UUID> allDecksIds = null;

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public boolean deckExists(String deckName) throws IOException, InterruptedException {
        if (allDecksIds == null) {
            // Fill the complete list of decks
            fetchAllDecksMetadata();
        }

        for (UUID deckId: allDecksIds) {
            assert deckMetadata.containsKey(deckId);

            if (deckMetadata.get(deckId).name().equals(deckName)) {
                return true;
            }
        }

        return false;
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

        allDecksIds = new HashSet<>();

        List<DeckMetadata> decks =  stringToDeckArray(response.body());
        for (DeckMetadata deck: decks) {
            allDecksIds.add(deck.id());
            deckMetadata.put(deck.id(), deck);
        }
    }

    public List<DeckMetadata> getAllDecks()
            throws IOException, InterruptedException {

        if (allDecksIds == null) {
            fetchAllDecksMetadata();
        }

        List<DeckMetadata> decks = new ArrayList<>();
        for (UUID deckId: allDecksIds) {
            decks.add(new DeckMetadata(deckMetadata.get(deckId)));
        }

        return decks;
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

        Pattern pattern = Pattern.compile(".*%s.*".formatted(deckName));
        List<DeckMetadata> decks = new ArrayList<>();
        for (DeckMetadata deck: getAllDecks()) {
            if (pattern.matcher(deck.name()).matches()) {
                decks.add(new DeckMetadata(deck));
            }
        }

        return decks;
    }

    public void deleteDeck(DeckMetadata deck)
            throws IOException, InterruptedException {

        String query =  "?deck_id=" + deck.id();
        HttpResponse<String> response
                = delete(ServerPaths.DELETE_DECK_PATH + query);

        checkResponseCode(response.statusCode());

        // Update cache
        cachedDecks.remove(deck.id());
        if (allDecksIds != null)
            allDecksIds.remove(deck.id());
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        HttpResponse<String> response
                = post(ServerPaths.SAVE_DECK_PATH, new Gson().toJson(deck));

        checkResponseCode(response.statusCode());

        // Update cache
        cachedDecks.put(deck.getId(), deck);
        allDecksIds.add(deck.getId());
        deckMetadata.put(deck.getId(), deck.getMetadata());
    }

    private void fetchDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {
        String path = ServerPaths.GET_DECK_PATH;
        String parameters = "?deck_id=%s".formatted(deckMetadata.id());
        HttpResponse<String> response = get(path + parameters);

        Deck receivedDeck = new Gson().fromJson(response.body(), Deck.class);
        cachedDecks.put(deckMetadata.id(), receivedDeck);
    }

    public Deck getDeck(DeckMetadata deckMetadata)
            throws IOException, InterruptedException {
        if (!cachedDecks.containsKey(deckMetadata.id())) {
            fetchDeck(deckMetadata);
        }
        return cachedDecks.get(deckMetadata.id());
    }
}
