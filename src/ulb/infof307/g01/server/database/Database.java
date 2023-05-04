package ulb.infof307.g01.server.database;

import ulb.infof307.g01.model.gamehistory.GameHistory;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.leaderboard.DeckLeaderboard;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboard;
import ulb.infof307.g01.server.database.dao.*;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Facade for all actions related to the database
 */
public class Database {
    DatabaseAccess databaseAccess;
    DeckDAO deckDao;
    TagDAO tagDao;
    UserDAO userDao;
    ScoreDAO scoreDao;
    MarketplaceDAO marketplaceDao;

    public Database() {
        this.databaseAccess = new DatabaseAccess();
        this.deckDao = new DeckDAO(this.databaseAccess);
        this.tagDao = new TagDAO(this.databaseAccess);
        this.userDao = new UserDAO(this.databaseAccess);
        this.scoreDao = new ScoreDAO(this.databaseAccess);
        this.marketplaceDao = new MarketplaceDAO(this.databaseAccess);

        this.deckDao.setTagDao(this.tagDao);
        this.tagDao.setDeckDao(this.deckDao);
        this.scoreDao.setUserDAO(this.userDao);
        this.marketplaceDao.setDeckDAO(this.deckDao);
    }

    public void open(File dbname) {
        this.databaseAccess.open(dbname);
    }

    public void initServerScheme() {
        this.databaseAccess.initTables(DatabaseSchema.SERVER);
    }

    /* ====================================================================== */
    /*                              Deck                                      */
    /* ====================================================================== */

    public boolean deckNameExists(String name, UUID userid) throws DatabaseException {
        return deckDao.deckNameExists(name, userid);
    }

    public void saveDeck(Deck deck, UUID userId) throws DatabaseException {
        deckDao.saveDeck(deck, userId);
    }

    public Deck getDeck(UUID deckId) throws DatabaseException {
        return deckDao.getDeck(deckId);
    }

    public List<DeckMetadata> getAllUserDecksMetadata(UUID userId) throws DatabaseException {
        return deckDao.getAllUserDecksMetadata(userId);
    }

    public UUID getDeckOwnerId(UUID deckId) throws DatabaseException {
        return deckDao.getDeckOwnerId(deckId);
    }

    public void deleteDeck(UUID deckId, UUID userId) throws DatabaseException {
        deckDao.deleteDeck(deckId, userId);
    }
    public List<DeckMetadata> searchDecksMetadata(String userSearch, UUID userId) throws DatabaseException {
        return deckDao.searchDecksMetadata(userSearch, userId);
    }

    public boolean deckIdExists(UUID deckId) throws DatabaseException {
        return deckDao.deckIdExists(deckId);
    }


    /* ====================================================================== */
    /*                              User                                      */
    /* ====================================================================== */

    public boolean loginUser(String username, String password) throws DatabaseException {
        return userDao.loginUser(username, password);
    }

    public boolean registerUser(String username, String password) {
        return userDao.registerUser(username, password);
    }

    public String getUserId(String username) {
        return userDao.getUserId(username);
    }


    /* ====================================================================== */
    /*                              Score/Leaderboard                         */
    /* ====================================================================== */

    public void saveScore(Score score) {
        scoreDao.addScore(score);
    }

    public void deleteScoresForDeck(UUID deckId) {
        scoreDao.deleteScoresForDeck(deckId);
    }

    public DeckLeaderboard getLeaderboardFromDeckId(UUID deckId) {
        return new DeckLeaderboard(deckId, scoreDao.getScoresForDeck(deckId));
    }

    public GlobalLeaderboard getGlobalLeaderboard() {
        return new GlobalLeaderboard(scoreDao.getAllUserDeckScore());
    }

    public GameHistory getGameHistory(UUID userId) {
        return new GameHistory(scoreDao.getGameHistory(userId));
    }

    public GameHistory getGameHistory(UUID userId, UUID deckId) {
        return new GameHistory(scoreDao.getGameHistory(userId, deckId));
    }


    /* ====================================================================== */
    /*                              Marketplace                                     */
    /* ====================================================================== */

    public List<MarketplaceDeckMetadata> getMarketplaceDecksMetadata() throws DatabaseException {
        return marketplaceDao.getMarketplaceDecksMetadata();
    }

    public void addDeckToMarketplace(UUID deckId) throws DatabaseException {
        marketplaceDao.addDeckToMarketplace(deckId);
    }

    public void removeDeckFromMarketplace(UUID deckId) throws DatabaseException {
        marketplaceDao.removeDeckFromMarketplace(deckId);
    }

    public void removeDeckFromNonOwnerCollection(UUID deckId, UUID ownerId) throws DatabaseException {
        marketplaceDao.removeDeckFromNonOwnerCollection(deckId, ownerId);
    }

    public void addDeckToUserCollection(UUID deckId, UUID userId) throws DatabaseException {
        marketplaceDao.addDeckToUserCollection(deckId, userId);
    }

    public void removeDeckFromUserCollection(UUID deckId, UUID userId) throws DatabaseException {
        marketplaceDao.removeDeckFromUserCollection(deckId, userId);
    }

    public List<MarketplaceDeckMetadata> getUsersCollectionFromMarketplace(UUID userId) throws DatabaseException {
        return marketplaceDao.getSavedDecks(userId);
    }

    public int getNumberOfPublicPlayedDecks(UUID userId) {
        return marketplaceDao.getNumberOfPublicPlayedDecks(userId);
    }
}
