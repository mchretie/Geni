package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TagManager {

    private static TagManager instance;

    private final static Database database = Database.singleton();
    private final static DeckManager deckManager = DeckManager.singleton();

    public static TagManager singleton() {
        if (instance == null)
            instance = new TagManager();
        return instance;
    }

    public void saveTag(Tag tag) {
        String sql = """
            INSERT INTO tag (tag_id, name, color)
            VALUES ('%1$s', '%2$s', '%3$s')
            ON CONFLICT (tag_id)
            DO UPDATE SET name = '%2$s', color = '%3$s'
            """.formatted(
                    tag.getId(),
                    tag.getName(),
                    tag.getColor());

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void deleteTag(Tag tag) {
        String sql = """
            DELETE FROM tag
            WHERE tag_id = '%1$s'
            """.formatted(tag.getId());

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
     * @see ulb.infof307.g01.database.DeckManager;
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

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeTagFrom(Deck deck, Tag tag) {
        String sql = """
            DELETE FROM deck_tag
            WHERE deck_id = '%1$s'
            """.formatted(deck.getId().toString());

        try {
            database.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                decks.add(deckManager.getDeck(deckId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return decks;
    }
}
