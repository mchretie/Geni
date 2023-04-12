package ulb.infof307.g01.server.database.dao;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.server.database.DatabaseAccess;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * Save and retrieve tags from long-term memory
 * <p>
 * The aimed workflow with this manager is to
 * use an object of class Tag, apply changes
 * on it before committing them to long-term
 * with saveTag or deleteTag.
 * <p>
 * To add tags to decks, see DeckManager.
 * <p>
 * Do not use directly, use the Database facade instead.
 * @see ulb.infof307.g01.server.database.Database
 */
public class TagDAO extends DAO {

    private final DatabaseAccess database;
    private DeckDAO deckDao;

    public TagDAO(DatabaseAccess database) {
        this.database = database;
    }

    public void setDeckDao(DeckDAO deckDao) {
        this.deckDao = deckDao;
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
    public boolean isTagValid(Tag tag) throws DatabaseException {
        if (tag == null)
            return false;

        String sql = """
                SELECT tag_id, name
                FROM tag
                WHERE NOT tag_id = ? AND name = ?
                """;

        return !checkedNext(database.executeQuery(sql,
                                                  tag.getId().toString(),
                                                  tag.getName()));
    }

    public boolean tagNameExists(String name) throws DatabaseException {
        String sql = """
                SELECT name
                FROM tag
                WHERE name = ?
                """;

        return checkedNext(database.executeQuery(sql, name));
    }


    /**
     * <p>
     * Upon the saving of multiple tags with the same name,
     * but with different ids, only the first will be saved while
     * the following will be ignored.
     */
    public void saveTag(Tag tag) throws DatabaseException {

        if (!isTagValid(tag))
            return;

        String sql = """
                INSERT INTO tag (tag_id, name, color)
                VALUES (?, ?, ?)
                ON CONFLICT (tag_id)
                DO UPDATE SET name = ?, color = ?
                ON CONFLICT(name)
                DO NOTHING
                """;

        database.executeUpdate(sql,
                               tag.getId().toString(),
                               tag.getName(),
                               tag.getColor(),
                               tag.getName(),
                               tag.getColor());
    }

    public Tag getTag(UUID uuid) throws DatabaseException {
        String sql = """
                SELECT tag_id, name, color
                FROM tag
                WHERE tag_id = ?
                """;

        ResultSet res = database.executeQuery(sql, uuid.toString());
        if (!checkedNext(res))
            return null;
        return extractTag(res);
    }


    public List<Tag> getAllTags() throws DatabaseException {
        String sql = """
                SELECT tag_id
                FROM tag
                """;

        ResultSet res = database.executeQuery(sql);
        List<UUID> tagIds = extractUUIDsFrom(res, "tag_id");
        return getTags(tagIds);
    }

    /**
     * Delete tag and all its associations to decks
     */
    public void deleteTag(Tag tag) throws DatabaseException {
        String sql = """
                DELETE FROM tag
                WHERE tag_id = ?
                """;

        database.executeUpdate(sql, tag.getId().toString());
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
    public void saveTagsFor(Deck deck) throws DatabaseException {

        Set<Tag> actualTags = deck.getTags().stream()
                .map(this::replaceIdIfAlreadyExist)
                .collect(toSet());

        for (Tag tag : actualTags) {
            saveTag(tag);
        }

        HashSet<Tag> currentTags = new HashSet<>(getTagsFor(deck.getId()));
        HashSet<Tag> newTags = new HashSet<>(actualTags);

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
     * Assumes the association doesn't exist in the database.
     */
    private void addTagTo(Deck deck, Tag tag) throws DatabaseException {
        String sql = """
                INSERT INTO deck_tag (deck_id, tag_id)
                VALUES (?, ?)
                """;

        database.executeUpdate(sql,
                               deck.getId().toString(),
                               tag.getId().toString());
    }

    private void removeTagFrom(Deck deck, Tag tag) throws DatabaseException {
        String sql = """
                DELETE FROM deck_tag
                WHERE deck_id = ?
                AND tag_id = ?
                """;

        database.executeUpdate(sql,
                               deck.getId().toString(),
                               tag.getId().toString());
    }

    /**
     * <p>
     * Should only be used by other managers.
     */
    public List<Tag> getTagsFor(UUID deckId) throws DatabaseException {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE deck_id = ?
                """;

        ResultSet res = database.executeQuery(sql, deckId.toString());
        List<UUID> tagIds = extractUUIDsFrom(res, "tag_id");
        return getTags(tagIds);
    }

    /**
     * For filtering purposes
     */
    public List<Deck> getDecksHavingTag(Tag tag) throws DatabaseException {
        String sql = """
                SELECT deck_id, tag_id
                FROM deck_tag
                WHERE tag_id = ?
                """;

        ResultSet res = database.executeQuery(sql, tag.getId().toString());
        List<UUID> deckIds = extractUUIDsFrom(res, "deck_id");
        return deckDao.getDecks(deckIds);
    }

    public List<Tag> searchTags(String userSearch) throws DatabaseException {
        String sql = """
                SELECT tag_id
                FROM tag
                WHERE name LIKE ?
                """;

        String pattern = userSearch + "%";
        ResultSet res = database.executeQuery(sql, pattern);
        List<UUID> tagIds = extractUUIDsFrom(res, "tag_id");
        return getTags(tagIds);
    }

    private List<Tag> getTags(List<UUID> res) throws DatabaseException {
        List<Tag> tags = new ArrayList<>();
        for (UUID tagId : res)
            tags.add(getTag(tagId));
        return tags;
    }

    private Tag extractTag(ResultSet res) {
        try {
            UUID uuid = UUID.fromString(res.getString("tag_id"));
            String name = res.getString("name");
            String color = res.getString("color");
            return new Tag(name, uuid, color);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private Tag replaceIdIfAlreadyExist(Tag tag) throws DatabaseException {
        String sql = """
                SELECT tag_id, name, color
                FROM tag
                WHERE name = ?
                """;

        ResultSet res = database.executeQuery(sql, tag.getName());
        if (checkedNext(res))
            tag = extractTag(res);
        return tag;
    }
}

