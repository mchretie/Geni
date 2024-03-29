package ulb.infof307.g01.gui.http.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ulb.infof307.g01.model.IndulgentValidator;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.rating.*;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class MarketplaceDAO extends HttpDAO {

    private final IndulgentValidator validator = new IndulgentValidator();


    /* ====================================================================== */
    /*                          Fetching methods                              */
    /* ====================================================================== */

    public List<MarketplaceDeckMetadata> getAllMarketplaceDecks() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_MARKETPLACE_PATH);

        checkResponseCode(response.statusCode());

        TypeToken<List<MarketplaceDeckMetadata>> typeToken = new TypeToken<>() {
        };
        return new Gson().fromJson(response.body(), typeToken);
    }

    public void addDeckToMarketplace(Deck deck) throws IOException, InterruptedException {
        String deckJson = new Gson().toJson(deck);

        HttpResponse<String> response
                = post(ServerPaths.ADD_DECK_TO_MARKETPLACE_PATH, deckJson);

        checkResponseCode(response.statusCode());
    }

    public void removeDeckFromMarketplace(MarketplaceDeckMetadata deck) throws IOException, InterruptedException {
        String path = ServerPaths.REMOVE_DECK_FROM_MARKETPLACE_PATH + "?deck_id=" + deck.id().toString();

        HttpResponse<String> response = delete(path);

        checkResponseCode(response.statusCode());
    }

    public List<MarketplaceDeckMetadata> getSavedDecks() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_SAVED_DECKS_FROM_MARKETPLACE);

        checkResponseCode(response.statusCode());

        TypeToken<List<MarketplaceDeckMetadata>> typeToken = new TypeToken<>() {
        };
        return new Gson().fromJson(response.body(), typeToken);
    }

    public List<MarketplaceDeckMetadata> searchDecks(String deckName)
            throws IOException, InterruptedException {

        if (deckName.isEmpty())
            return getAllMarketplaceDecks();

        final Pattern pattern = Pattern.compile("%s.*".formatted(validator.addTolerance(deckName)));

        return getAllMarketplaceDecks().stream()
                .filter(deck -> pattern.matcher(validator.addTolerance(deck.name())).matches())
                .collect(toList());
    }

    public List<MarketplaceDeckMetadata> searchDecksByCreator(String Creator)
            throws IOException, InterruptedException {

        if (Creator.isEmpty())
            return getAllMarketplaceDecks();

        final Pattern pattern = Pattern.compile("%s.*".formatted(validator.addTolerance(Creator)));

        return getAllMarketplaceDecks().stream()
                .filter(deck -> pattern.matcher(validator.addTolerance(deck.owner())).matches())
                .collect(toList());
    }

    public List<MarketplaceDeckMetadata> searchDecksByTag(String tagName)
            throws IOException, InterruptedException {

        if (tagName.isEmpty())
            return getAllMarketplaceDecks();

        final Pattern pattern = Pattern.compile("%s.*".formatted(validator.addTolerance(tagName)));

        return getAllMarketplaceDecks().stream()
                .filter(deck -> deck.tags().stream()
                        .anyMatch(tag -> pattern.matcher(validator.addTolerance(tag.getName())).matches()))
                .collect(toList());
    }

    public void addRating(UserRating userRating) throws IOException, InterruptedException {
        String ratingJson = userRating.toJson();

        HttpResponse<String> response = post(ServerPaths.ADD_RATING, ratingJson);

        checkResponseCode(response.statusCode());
    }

    public UserRating getUserRating(DeckMetadata deckMetadata) throws IOException, InterruptedException {
        String path = ServerPaths.GET_USER_RATING;
        path += "?deck_id=" + deckMetadata.id().toString();

        HttpResponse<String> response = get(path);
        checkResponseCode(response.statusCode());

        return UserRating.fromJson(response.body());
    }
}
