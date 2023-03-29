package ulb.infof307.g01.server.database;

import ulb.infof307.g01.server.database.dao.*;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.io.File;
import java.sql.SQLException;
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

    public Database() {
        this.databaseAccess = new DatabaseAccess();
        this.deckDao = new DeckDAO(this.databaseAccess);
        this.tagDao = new TagDAO(this.databaseAccess);
        this.userDao = new UserDAO(this.databaseAccess);

        this.deckDao.setTagDao(this.tagDao);
        this.tagDao.setDeckDao(this.deckDao);
    }

    public void open(File dbname) {
        this.databaseAccess.open(dbname);
    }

    public void initServerScheme() {
        this.databaseAccess.initTables(DatabaseScheme.SERVER);
    }

    /* ====================================================================== */
    /*                              Deck                                      */
    /* ====================================================================== */

    public boolean isDeckValid(Deck deck) throws DatabaseException {
        return deckDao.isDeckValid(deck);
    }

    public boolean deckNameExists(String name) throws DatabaseException {
        return deckDao.deckNameExists(name);
    }

    public void saveDeck(Deck deck, UUID userId) throws DatabaseException {
        deckDao.saveDeck(deck, userId);
    }

    public Deck getDeck(UUID uuid) throws DatabaseException {
        return deckDao.getDeck(uuid);
    }

    public List<Deck> getAllDecks() throws DatabaseException {
        return deckDao.getAllDecks();
    }

    public List<Deck> getAllUserDecks(UUID userId) throws DatabaseException {
        return deckDao.getAllUserDecks(userId);
    }

    public void deleteDeck(UUID deckId, UUID userId) throws DatabaseException {
        deckDao.deleteDeck(deckId, userId);
    }

    public List<Deck> searchDecks(String userSearch) throws DatabaseException {
        return deckDao.searchDecks(userSearch);
    }


    /* ====================================================================== */
    /*                              Tag                                       */
    /* ====================================================================== */

    public boolean isTagValid(Tag tag) throws DatabaseException {
        return tagDao.isTagValid(tag);
    }

    public boolean tagNameExists(String name) throws DatabaseException {
        return tagDao.tagNameExists(name);
    }

    public void saveTag(Tag tag) throws DatabaseException {
        tagDao.saveTag(tag);
    }

    public Tag getTag(UUID uuid) throws DatabaseException {
        return tagDao.getTag(uuid);
    }

    public List<Tag> getAllTags() throws DatabaseException {
        return tagDao.getAllTags();
    }

    public void deleteTag(Tag tag) throws DatabaseException {
        tagDao.deleteTag(tag);
    }

    public void saveTagsFor(Deck deck) throws DatabaseException {
        tagDao.saveTagsFor(deck);
    }

    public List<Tag> getTagsFor(UUID deckId) throws DatabaseException {
        return tagDao.getTagsFor(deckId);
    }

    public List<Deck> getDecksHavingTag(Tag tag) throws DatabaseException {
        return tagDao.getDecksHavingTag(tag);
    }

    public List<Tag> searchTags(String userSearch) throws DatabaseException {
        return tagDao.searchTags(userSearch);
    }


    /* ====================================================================== */
    /*                              User                                      */
    /* ====================================================================== */

    public void registerGuest(UUID guestId) throws DatabaseException {
        userDao.registerGuest(guestId);
    }
}
