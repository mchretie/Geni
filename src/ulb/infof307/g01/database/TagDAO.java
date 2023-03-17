package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

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
     * @see TagDAO.tagNameExists
     */
    public boolean isTagValid(Tag tag) {
        String sql = """
            SELECT tag_id, name
            FROM tag
            WHERE NOT tag_id = '%1$s' AND name = '%2$s'
            """.formatted(tag.getId().toString(),
            tag.getName());

        try {
            return !database.executeQuery(sql).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean tagNameExists(String name) {
        String sql = """
            SELECT name
            FROM tag
            WHERE name = '%1$s'
            """.formatted(name);

        try {
            return database.executeQuery(sql).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * Upon the saving of multiple tags with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    public void saveTag(Tag tag) {
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

    public Tag getTag(UUID uuid) {
        String sql = """
                SELECT tag_id, name, color
                FROM tag
                WHERE tag_id = '%1$s'
                """.formatted(uuid.toString());

        Tag tag = null;

        try {
            ResultSet res = database.executeQuery(sql);
            if (res.next()) {
                String name = res.getString("name");
                String color = res.getString("color");
                tag = new Tag(name, uuid, color);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tag;
    }

    public List<Tag> getAllTags() {
        String sql = """
                SELECT tag_id
                FROM tag
                """;

        List<Tag> tags = new ArrayList<Tag>();

        try {
            ResultSet res = database.executeQuery(sql);
            while (res.next()) {
                UUID tagId = UUID.fromString(res.getString("tag_id"));
                tags.add(getTag(tagId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tags;
    }

    /**
     * Delete tag and all its associations to decks
     */
    public void deleteTag(Tag tag) {
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
    public void saveTagsFor(Deck deck) {
        deck.getTags().forEach((t) -> saveTag(t));

        HashSet<Tag> currentTags = new HashSet<Tag>(getTagsFor(deck.getId()));
        HashSet<Tag> newTags = new HashSet<Tag>(deck.getTags());

        Set<Tag> addedTags = (Set<Tag>) newTags.clone();
        addedTags.removeAll(currentTags);

        Set<Tag> deletedTags = (Set<Tag>) currentTags.clone();
        deletedTags.removeAll(newTags);

        addedTags.forEach((t) -> addTagTo(deck, t));
        deletedTags.forEach((t) -> removeTagFrom(deck, t));
    }

    /**
     * Assumes the association doesn’t exist in the database.
     */
    private void addTagTo(Deck deck, Tag tag) {
        String sql = """
                INSERT INTO deck_tag (deck_id, tag_id)
                VALUES ('%1$s', '%2$s')
                """.formatted(
                deck.getId().toString(),
                tag.getId().toString());

        database.executeUpdate(sql);
    }

    private void removeTagFrom(Deck deck, Tag tag) {
        String sql = """
                DELETE FROM deck_tag
                WHERE deck_id = '%1$s'
                """.formatted(deck.getId().toString());

        database.executeUpdate(sql);
    }

    /**
     * <p>
     * Should only be used by other managers.
     */
    public List<Tag> getTagsFor(UUID deckId) {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE deck_id = '%1$s'
                """.formatted(deckId.toString());

        List<Tag> tags = new ArrayList();

        try {
            ResultSet res = database.executeQuery(sql);
            while (res.next()) {
                UUID tagId = UUID.fromString(res.getString("tag_id"));
                tags.add(getTag(tagId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tags;
    }

    /**
     * For filtering purposes
     */
    public List<Deck> getDecksHavingTag(Tag tag) {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE tag_id = '%1$s'
                """.formatted(tag.getId().toString());

        List<Deck> decks = new ArrayList();

        try {
            ResultSet res = database.executeQuery(sql);
            while (res.next()) {
                UUID deckId = UUID.fromString(res.getString("deck_id"));
                decks.add(deckDao.getDeck(deckId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return decks;
    }
}
