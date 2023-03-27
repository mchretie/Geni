package ulb.infof307.g01.server.database;

import ulb.infof307.g01.server.database.dao.*;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Database {

    static Database instance;

    // DatabaseConnectionManager conManager;
    DeckDAO deckDao;
    TagDAO tagDao;
    UserDAO userDao;

    // private Database(DatabaseConnectionManager conManager) {
    //     this.conManager = conManager;
    //     this.deckDao = new DeckDAO();
    //     this.tagDao = new TagDAO();
    //     this.userDao = new UserDAO();
    // }

    private Database() {
        this.deckDao = DeckDAO.singleton();
        this.tagDao = TagDAO.singleton();
        this.userDao = new UserDAO();
    }

    public static Database singleton() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    /* ====================================================================== */
    /*                              Deck                                      */
    /* ====================================================================== */

    public boolean isDeckValid(Deck deck) throws SQLException {
        return deckDao.isDeckValid(deck);
    }

    public boolean deckNameExists(String name) throws SQLException {
        return deckDao.deckNameExists(name);
    }

    public void saveDeck(Deck deck, UUID userId) throws SQLException {
        deckDao.saveDeck(deck, userId);
    }

    public Deck getDeck(UUID uuid) throws SQLException {
        return deckDao.getDeck(uuid);
    }

    public List<Deck> getAllDecks() throws SQLException {
        return deckDao.getAllDecks();
    }

    public List<Deck> getAllUserDecks(UUID userId) throws SQLException {
        return deckDao.getAllUserDecks(userId);
    }

    public void deleteDeck(UUID deckId, UUID userId) throws SQLException {
        deckDao.deleteDeck(deckId, userId);
    }

    public List<Deck> searchDecks(String userSearch) throws SQLException {
        return deckDao.searchDecks(userSearch);
    }


    /* ====================================================================== */
    /*                              Tag                                       */
    /* ====================================================================== */

    public boolean isTagValid(Tag tag) throws SQLException {
        return tagDao.isTagValid(tag);
    }

    public boolean tagNameExists(String name) throws SQLException {
        return tagDao.tagNameExists(name);
    }

    public void saveTag(Tag tag) throws SQLException {
        tagDao.saveTag(tag);
    }

    public Tag getTag(UUID uuid) throws SQLException {
        return tagDao.getTag(uuid);
    }

    public List<Tag> getAllTags() throws SQLException {
        return tagDao.getAllTags();
    }

    public void deleteTag(Tag tag) throws SQLException {
        tagDao.deleteTag(tag);
    }

    public void saveTagsFor(Deck deck) throws SQLException {
        tagDao.saveTagsFor(deck);
    }

    public List<Tag> getTagsFor(UUID deckId) throws SQLException {
        return tagDao.getTagsFor(deckId);
    }

    public List<Deck> getDecksHavingTag(Tag tag) throws SQLException {
        return tagDao.getDecksHavingTag(tag);
    }

    public List<Tag> searchTags(String userSearch) throws SQLException {
        return tagDao.searchTags(userSearch);
    }


    /* ====================================================================== */
    /*                              User                                      */
    /* ====================================================================== */

    public void registerGuest(UUID guestId) throws SQLException {
        userDao.registerGuest(guestId);
    }

}
