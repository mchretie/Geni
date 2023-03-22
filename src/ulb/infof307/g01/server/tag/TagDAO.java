package ulb.infof307.g01.server.tag;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.deck.DeckDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Save and retrieve tags from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Tag, apply changes
 * on it before commiting them to long-term
 * with saveTag or deleteTag.
 * <p>
 * To add tags to decks, see DeckManager.
 *
 * @see DeckDAO
 */
public class TagDAO {

    // Singleton
    private static TagDAO instance;

    private final static Database database = Database.singleton();
    private final static DeckDAO deckDao = DeckDAO.singleton();

    protected TagDAO() {
    }

    public static TagDAO singleton() {
        if (instance == null)
            instance = new TagDAO();
        return instance;
    }

    /**
     * <p>
     * A tag is invalid if there exists a tag with
     * the same name but a different id in the database.
     * <p>
     * This may happen when a tag created outside
     * this class has the same name as one in the database.
     * This can be avoided by checking for uniqueness
     * beforehand.
     *
     * @see TagDAO#tagNameExists
     */
    public boolean isTagValid(Tag tag) throws SQLException {

        if (tag == null)
            return false;

        String sql = """
            SELECT tag_id, name
            FROM tag
            WHERE NOT tag_id = '%1$s' AND name = '%2$s'
            """.formatted(tag.getId().toString(),
                tag.getName());

        return !database.executeQuery(sql).next();
    }

    public boolean tagNameExists(String name) throws SQLException {
        String sql = """
            SELECT name
            FROM tag
            WHERE name = '%1$s'
            """.formatted(name);

        return database.executeQuery(sql).next();
    }

    /**
     * <p>
     * Upon the saving of multiple tags with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    public void saveTag(Tag tag) throws SQLException {

        if (!isTagValid(tag))
            return;

        String sql = """
                INSERT INTO tag (tag_id, name, color)
                VALUES ('%1$s', '%2$s', '%3$s')
                ON CONFLICT (tag_id)
                DO UPDATE SET name = '%2$s', color = '%3$s'
                ON CONFLICT(name)
                DO NOTHING
                """.formatted(
                tag.getId(),
                tag.getName(),
                tag.getColor());

        database.executeUpdate(sql);
    }

    public Tag getTag(UUID uuid) throws SQLException {
        String sql = """
                SELECT tag_id, name, color
                FROM tag
                WHERE tag_id = '%1$s'
                """.formatted(uuid.toString());

        Tag tag;

        try (ResultSet res = database.executeQuery(sql)) {
            if (!res.next())
                return null;

            String name = res.getString("name");
            String color = res.getString("color");
            tag = new Tag(name, uuid, color);
        }

        return tag;
    }

    public List<Tag> getAllTags() throws SQLException {
        String sql = """
                SELECT tag_id
                FROM tag
                """;

        return getTags(sql);
    }

    private List<Tag> getTags(String sql) throws SQLException {
        List<Tag> tags = new ArrayList<>();

        try(ResultSet res = database.executeQuery(sql)) {
            while (res.next()) {
                UUID tagId = UUID.fromString(res.getString("tag_id"));
                tags.add(getTag(tagId));
            }
        }

        return tags;
    }

    /**
     * Delete tag and all its associations to decks
     */
    public void deleteTag(Tag tag) throws SQLException {
        String sql = """
                DELETE FROM tag
                WHERE tag_id = '%1$s'
                """.formatted(tag.getId());

        database.executeUpdate(sql);
    }

    /**
     * Saves tags associated with a given deck
     * <p>
     * If the tags do not exist before the call,
     * they are created.
     * <p>
     * Should not be called directly from client code,
     * use the facilities from DeckManager such
     * as saveDeck.
     *
     * @see DeckDAO
     */
    @SuppressWarnings("unchecked")
    public void saveTagsFor(Deck deck) throws SQLException {

        for (Tag tag : deck.getTags()) {
            saveTag(tag);
        }

        HashSet<Tag> currentTags = new HashSet<>(getTagsFor(deck.getId()));
        HashSet<Tag> newTags = new HashSet<>(deck.getTags());

        Set<Tag> addedTags = (Set<Tag>) newTags.clone();
        addedTags.removeAll(currentTags);

        Set<Tag> deletedTags = (Set<Tag>) currentTags.clone();
        deletedTags.removeAll(newTags);

        for (Tag addedTag : addedTags)
            addTagTo(deck, addedTag);

        for (Tag t : deletedTags)
            removeTagFrom(deck, t);
    }

    /**
     * Assumes the association doesn’t exist in the database.
     */
    private void addTagTo(Deck deck, Tag tag) throws SQLException {
        String sql = """
                INSERT INTO deck_tag (deck_id, tag_id)
                VALUES ('%1$s', '%2$s')
                """.formatted(
                deck.getId().toString(),
                tag.getId().toString());

        database.executeUpdate(sql);
    }

    private void removeTagFrom(Deck deck, Tag tag) throws SQLException {
        String sql = """
                DELETE FROM deck_tag
                WHERE deck_id = '%1$s'
                AND tag_id = '%2$s'
                """.formatted(
                deck.getId().toString(),
                tag.getId().toString());

        database.executeUpdate(sql);
    }

    /**
     * <p>
     * Should only be used by other managers.
     */
    public List<Tag> getTagsFor(UUID deckId) throws SQLException {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE deck_id = '%1$s'
                """.formatted(deckId.toString());

        return getTags(sql);
    }

    /**
     * For filtering purposes
     */
    public List<Deck> getDecksHavingTag(Tag tag) throws SQLException {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE tag_id = '%1$s'
                """.formatted(tag.getId().toString());

        List<Deck> decks = new ArrayList<>();

        try (ResultSet res = database.executeQuery(sql)) {
            while (res.next()) {
                UUID deckId = UUID.fromString(res.getString("deck_id"));
                decks.add(deckDao.getDeck(deckId));
            }
        }

        return decks;
    }

    public List<Tag> searchTags(String userSearch) throws SQLException {
        String sql = """
            SELECT tag_id
            FROM tag
            WHERE name LIKE '%s'
            """.formatted(userSearch + "%");

        return getTags(sql);
    }

}

