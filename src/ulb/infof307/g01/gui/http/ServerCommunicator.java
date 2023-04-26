package ulb.infof307.g01.gui.http;

import ulb.infof307.g01.gui.http.dao.*;
import ulb.infof307.g01.model.deck.Deck;
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
            throws IOException, InterruptedException {

        userSessionDAO.login(username, password);
        setJWT();
    }

    public void userLogout() {
        userSessionDAO.logout();
        removeJWT();
    }

    public void userRegister(String username, String password)
            throws IOException, InterruptedException {

        userSessionDAO.register(username, password);
    }

    public String getSessionUsername() {
        return userSessionDAO.getUsername();
    }

    public boolean isUserLoggedIn() {
        return userSessionDAO.isLoggedIn();
    }

    public void attemptAutoLogin() throws IOException, InterruptedException {
        userSessionDAO.attemptAutoLogin();
        setJWT();
    }

    /* ====================================================================== */
    /*                              Game history                              */
    /* ====================================================================== */

    public GameHistory getGameHistory() throws IOException, InterruptedException {
        return gameHistoryDAO.getGameHistory();
    }

    public GameHistory getGameHistory(UUID deckId) throws IOException, InterruptedException {
        return gameHistoryDAO.getGameHistory(deckId);
    }

    /* ====================================================================== */
    /*                                 Score                                  */
    /* ====================================================================== */

    public void addScore(Score score) throws IOException, InterruptedException {
        scoreDAO.addScore(score);
    }

    public Score getBestScoreForDeck(UUID deckId) throws IOException, InterruptedException {
        return scoreDAO.getBestScoreForDeck(deckId);
    }

    public GlobalLeaderboard getGlobalLeaderboard() throws IOException, InterruptedException {
        return scoreDAO.getGlobalLeaderboard();
    }

    /* ====================================================================== */
    /*                                  Deck                                  */
    /* ====================================================================== */

    public boolean deckNameExists(String name)
            throws IOException, InterruptedException {

        return deckDAO.deckExists(name);
    }

    public List<DeckMetadata> getAllDecksMetadata()
            throws IOException, InterruptedException {

        return deckDAO.getAllDecksMetadata();
    }

    public List<DeckMetadata> searchDecks(String deckName)
            throws IOException, InterruptedException {

        return deckDAO.searchDecks(deckName);
    }

    public List<DeckMetadata> searchDecksByTags(String tagName)
            throws IOException, InterruptedException {

        return deckDAO.searchDecksByTags(tagName);
    }

    public void removeDeckFromCollection(UUID deckId)
            throws IOException, InterruptedException {

        deckDAO.removeDeckFromCollection(deckId);
    }

    public void addDeckToCollection(UUID deckId)
            throws IOException, InterruptedException {

        deckDAO.addDeckToCollection(deckId);
    }

    public void saveDeck(Deck deck)
            throws IOException, InterruptedException {

        deckDAO.saveDeck(deck);
    }

    public Optional<Deck> getDeck(DeckMetadata deckId)
            throws IOException, InterruptedException {

        return deckDAO.getDeck(deckId);
    }

    public int getDeckCount() throws IOException, InterruptedException {
        return deckDAO.getDeckCount();
    }

    public void uploadImage(File image, String filename)
            throws IOException, InterruptedException {

        deckDAO.uploadImage(image, filename);
    }

    /* ====================================================================== */
    /*                              Marketplace                               */
    /* ====================================================================== */

    public List<MarketplaceDeckMetadata> getAllMarketplaceDecks()
            throws IOException, InterruptedException {

        return marketplaceDAO.getAllMarketplaceDecks();
    }

    public void addDeckToMarketplace(Deck deck)
            throws IOException, InterruptedException {

        marketplaceDAO.addDeckToMarketplace(deck);
        deckDAO.emptyCache();
    }

    public void addMarketplaceDeckToCollection(UUID deck) throws IOException, InterruptedException {
        deckDAO.addDeckToCollection(deck);
        deckDAO.emptyCache();
    }

    public void removeDeckFromMarketplace(String deckId)
            throws IOException, InterruptedException {

        marketplaceDAO.removeDeckFromMarketplace(deckId);
    }

    public List<MarketplaceDeckMetadata> getSavedDecks()
            throws IOException, InterruptedException {

        return marketplaceDAO.getSavedDecks();
    }
}
