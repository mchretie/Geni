package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagManager {

    private static TagManager tm;

    public static TagManager singleton() {
        if (tm == null) {
            tm = new TagManager();
        }
        return tm;
    }

    boolean tagNotExists(UUID uuid) throws DatabaseNotInitException {
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT count(*) FROM tag WHERE tag_id = " + '"' + uuid + '"');
            if (response.next()) {
                return response.getInt("count(*)") == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Tag getTag(UUID uuid) throws TagNotExistsException, DatabaseNotInitException {
        if (tagNotExists(uuid)) {
            throw new TagNotExistsException("Could not find requested tag");
        }
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, tag_id, color FROM tag WHERE tag_id = " + '"' + uuid + '"');
            if (response.next()) {
                return new Tag(response.getString("name"), UUID.fromString(response.getString("tag_id")), response.getString("color"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Tag> getTagsFor(UUID deckUuid) throws DatabaseNotInitException, DeckNotExistsException {
        List<Tag> tags = new ArrayList<>();
        try {
            if (DeckManager.singleton().deckNotExists(deckUuid)) {
                throw new DeckNotExistsException("Could not find requested deck");
            }
            ResultSet response = Database.singleton().executeQuery("SELECT name, tag_id, color FROM tag WHERE tag_id IN (SELECT tag_id FROM deck_tag WHERE deck_id = " + '"' + deckUuid + '"' + ")");
            while (response.next()) {
                tags.add(new Tag(response.getString("name"), UUID.fromString(response.getString("tag_id")), response.getString("color")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tags;
    }

    public List<Tag> getAllTags() throws DatabaseNotInitException {
        try {
            ResultSet response = Database.singleton().executeQuery("SELECT name, tag_id, color FROM tag");
            List<Tag> tags = new ArrayList<>();
            while (response.next()) {
                tags.add(new Tag(response.getString("name"), UUID.fromString(response.getString("tag_id")), response.getString("color")));
            }
            return tags;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTag(Deck deck, Tag tag) throws DatabaseNotInitException {
        try {
            Database.singleton().executeUpdate("INSERT INTO tag (name, tag_id, color) VALUES ('" + tag.getName() + "', '" + tag.getId() + "', '" + tag.getColor() + "')");
            Database.singleton().executeUpdate("INSERT INTO deck_tag (deck_id, tag_id) VALUES ('" + deck.getId() + "', '" + tag.getId() + "')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delTag(Tag tag) throws DatabaseNotInitException, TagNotExistsException {
        if (tagNotExists(tag.getId())) {
            throw new TagNotExistsException("Could not find requested tag");
        }
        try {
            Database.singleton().executeUpdate("DELETE FROM deck_tag WHERE tag_id = " + '"' + tag.getId() + '"');
            Database.singleton().executeUpdate("DELETE FROM tag WHERE tag_id = " + '"' + tag.getId() + '"');
        } catch (SQLException e) {
            throw new TagNotExistsException("Error deleting requested tag");
        }
    }

}
