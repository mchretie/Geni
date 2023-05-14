package ulb.infof307.g01.gui.http;

import ulb.infof307.g01.gui.http.dao.*;
import ulb.infof307.g01.gui.http.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.rating.UserRating;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.gamehistory.GameHistory;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboard;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A facade for all communication between client and server
 */
public class ServerCommunicator {

    private final DeckDAO deckDAO = new DeckDAO();
    private final UserSessionDAO userSessionDAO = new UserSessionDAO();
    private final GameHistoryDAO gameHistoryDAO = new GameHistoryDAO();
    private final ScoreDAO scoreDAO = new ScoreDAO();
    private final MarketplaceDAO marketplaceDAO = new MarketplaceDAO();

    /* ====================================================================== */
    /*                             User Session                               */
    /* ====================================================================== */

    private void setJWT() {
        String token = userSessionDAO.getJWT();
        deckDAO.setJWT(token);
        gameHistoryDAO.setJWT(token);
        scoreDAO.setJWT(token);
        marketplaceDAO.setJWT(token);
    }

    private void removeJWT() {
        deckDAO.removeJWT();
        gameHistoryDAO.removeJWT();
        scoreDAO.removeJWT();
        marketplaceDAO.removeJWT();
    }

    public void userLogin(String username, String password)
            throws ServerCommunicationFailedException {

        try {
            userSessionDAO.login(username, password);
            setJWT();

        } catch (IOException | InterruptedException e) {
            throw new AuthenticationFailedException("Failed to login");
        }
    }

    public void userLogout() {
        userSessionDAO.logout();
        removeJWT();
    }

    public void userRegister(String username, String password)
            throws ServerCommunicationFailedException {

        try {
            userSessionDAO.register(username, password);
            setJWT();

        } catch (IOException | InterruptedException e) {
            throw new AuthenticationFailedException("Failed to register");
        }
    }

    public String getSessionUsername() {
        return userSessionDAO.getUsername();
    }

    public boolean isUserLoggedIn() {
        return userSessionDAO.isLoggedIn();
    }

    public void attemptAutoLogin()
            throws ServerCommunicationFailedException {

        try {
            userSessionDAO.attemptAutoLogin();
            setJWT();

        } catch (IOException | InterruptedException e) {
            throw new AuthenticationFailedException("Failed to auto-login");
        }
    }

    /* ====================================================================== */
    /*                              Game history                              */
    /* ====================================================================== */

    public GameHistory getGameHistory()
            throws ServerCommunicationFailedException {

        try {
            return gameHistoryDAO.getGameHistory();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get game history";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public GameHistory getGameHistory(UUID deckId)
            throws ServerCommunicationFailedException {

        try {
            return gameHistoryDAO.getGameHistory(deckId);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get game history";
            throw new ServerCommunicationFailedException(message);
        }
    }

    /* ====================================================================== */
    /*                                 Score                                  */
    /* ====================================================================== */

    public void addScore(Score score)
            throws ServerCommunicationFailedException {

        try {
            scoreDAO.addScore(score);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to add score";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public Score getBestScoreForDeck(UUID deckId)
            throws ServerCommunicationFailedException {

       try {
           return scoreDAO.getBestScoreForDeck(deckId);

       } catch (IOException | InterruptedException e) {
           String message = "Failed to get best score for deck";
           throw new ServerCommunicationFailedException(message);
       }
    }

    public GlobalLeaderboard getGlobalLeaderboard()
            throws ServerCommunicationFailedException {

        try {
            return scoreDAO.getGlobalLeaderboard();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get global leaderboard";
            throw new ServerCommunicationFailedException(message);
        }
    }

    /* ====================================================================== */
    /*                                  Deck                                  */
    /* ====================================================================== */

    public boolean deckNameExists(String name)
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.deckExists(name);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to check if deck name exists";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<DeckMetadata> getAllDecksMetadata()
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.getAllDecksMetadata();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get all decks metadata";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<DeckMetadata> searchDecks(String deckName)
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.searchDecks(deckName);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to search decks";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<DeckMetadata> searchDecksByTags(String tagName)
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.searchDecksByTags(tagName);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to search decks by tags";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void deleteDeck(DeckMetadata deckMetadata) throws IOException, InterruptedException {
        deckDAO.deleteDeck(deckMetadata);
    }

    public void removeDeckFromCollection(DeckMetadata deckMetadata)
            throws ServerCommunicationFailedException {

        try {
            deckDAO.removeDeckFromCollection(deckMetadata);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to remove deck from collection";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void addDeckToCollection(DeckMetadata deckMetadata)
            throws ServerCommunicationFailedException {

        try {
            deckDAO.addDeckToCollection(deckMetadata);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to add deck to collection";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void saveDeck(Deck deck)
            throws ServerCommunicationFailedException {

        try {
            deckDAO.saveDeck(deck);


        } catch (IOException | InterruptedException e) {
            String message = "Failed to save deck";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public Optional<Deck> getDeck(DeckMetadata deckId)
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.getDeck(deckId);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get deck";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public int getDeckCount()
            throws ServerCommunicationFailedException {

        try {
            return deckDAO.getDeckCount();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get deck count";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void uploadImage(File image, String filename)
            throws ServerCommunicationFailedException {

        try {
            deckDAO.uploadImage(image, filename);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to upload image";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public int numberOfPublicPlayedDecks() throws ServerCommunicationFailedException {
        try {
            return deckDAO.numberOfPublicPlayedDecks();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get number of public played decks";
            throw new ServerCommunicationFailedException(message);
        }
    }

    /* ====================================================================== */
    /*                              Marketplace                               */
    /* ====================================================================== */

    public List<MarketplaceDeckMetadata> getAllMarketplaceDecks()
            throws ServerCommunicationFailedException {

       try {
           return marketplaceDAO.getAllMarketplaceDecks();

       } catch (IOException | InterruptedException e) {
           String message = "Failed to get all marketplace decks";
           throw new ServerCommunicationFailedException(message);
       }
    }

    public void addDeckToMarketplace(Deck deck)
            throws ServerCommunicationFailedException {

        try {
            marketplaceDAO.addDeckToMarketplace(deck);
            deckDAO.updateCache(deck.getMetadata());

        } catch (IOException | InterruptedException e) {
            String message = "Failed to add deck to marketplace";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void addMarketplaceDeckToCollection(DeckMetadata deck)
            throws ServerCommunicationFailedException {

        try {
            deckDAO.addDeckToCollection(deck);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to add marketplace deck to collection";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void removeDeckFromMarketplace(MarketplaceDeckMetadata deck)
            throws ServerCommunicationFailedException {

        try {
            //TODO : clean this up
            var deckMetadata = DeckMetadata.fromMarketplaceDeckMetadata(deck);
            Deck deckToRemove = deckDAO.getDeck(deckMetadata).orElseThrow();
            deckToRemove.switchOnlineVisibility();

            marketplaceDAO.removeDeckFromMarketplace(deck);
            deckDAO.updateCache(deckToRemove.getMetadata());
        } catch (IOException | InterruptedException e) {
            String message = "Failed to remove deck from marketplace";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<MarketplaceDeckMetadata> getSavedDecks()
            throws ServerCommunicationFailedException {

        try {
            return marketplaceDAO.getSavedDecks();

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get saved decks from server";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<MarketplaceDeckMetadata> searchDecksMarketplace(String deckName)
            throws ServerCommunicationFailedException {

        try {
            return marketplaceDAO.searchDecks(deckName);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to search decks";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public List<MarketplaceDeckMetadata> searchDecksMarketplaceByCreator(String deckName)
            throws ServerCommunicationFailedException {

        try {
            return marketplaceDAO.searchDecksByCreator(deckName);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to search decks";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public void addRating(UserRating userRating)
            throws ServerCommunicationFailedException {

        try {
            marketplaceDAO.addRating(userRating);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to add userRating to marketplace";
            throw new ServerCommunicationFailedException(message);
        }
    }

    public UserRating getUserRating(DeckMetadata deckMetadata)
            throws ServerCommunicationFailedException {

        try {
            return marketplaceDAO.getUserRating(deckMetadata);

        } catch (IOException | InterruptedException e) {
            String message = "Failed to get userRating from marketplace";
            throw new ServerCommunicationFailedException(message);
        }
    }
}
