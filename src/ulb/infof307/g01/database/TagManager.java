package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagManager {

    private static TagManager instance;

    private Database database = Database.singleton();

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
     * Should not be called directly from client code.
     *
     * @see ulb.infof307.g01.database.DeckManager;
     */
    public void saveTagsFor(Deck deck);

    public void getTagsFor(UUID deckId);


//     public List<Tag> getTagsFor(UUID deckUuid) throws DatabaseNotInitException, DeckNotExistsException {
//         List<Tag> tags = new ArrayList<>();
//         try {
//             // if (DeckManager.singleton().deckNotExists(deckUuid)) {
//             //     throw new DeckNotExistsException("Could not find requested deck");
//             // }
//             ResultSet response = database.executeQuery("SELECT name, tag_id, color FROM tag WHERE tag_id IN (SELECT tag_id FROM deck_tag WHERE deck_id = " + '"' + deckUuid + '"' + ")");
//             while (response.next()) {
//                 tags.add(new Tag(response.getString("name"), UUID.fromString(response.getString("tag_id")), response.getString("color")));
//             }
//         } catch (SQLException e) {
//             throw new RuntimeException(e);
//         }
//         return tags;
//     }


//     public void addTag(Deck deck, Tag tag) throws DatabaseNotInitException {
//         try {
//             database.executeUpdate("INSERT INTO tag (name, tag_id, color) VALUES ('" + tag.getName() + "', '" + tag.getId() + "', '" + tag.getColor() + "')");
//             database.executeUpdate("INSERT INTO deck_tag (deck_id, tag_id) VALUES ('" + deck.getId() + "', '" + tag.getId() + "')");
//         } catch (SQLException e) {
//             throw new RuntimeException(e);
//         }
//     }

//     public void delTag(Tag tag) throws DatabaseNotInitException, TagNotExistsException {
//         if (tagNotExists(tag.getId())) {
//             throw new TagNotExistsException("Could not find requested tag");
//         }
//         try {
//             database.executeUpdate("DELETE FROM deck_tag WHERE tag_id = " + '"' + tag.getId() + '"');
//             database.executeUpdate("DELETE FROM tag WHERE tag_id = " + '"' + tag.getId() + '"');
//         } catch (SQLException e) {
//             throw new TagNotExistsException("Error deleting requested tag");
//         }
//     }

}
