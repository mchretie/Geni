package ulb.infof307.g01.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagManager extends DatabaseUsingTest {

    TagManager tagManager = TagManager.singleton();
    DeckManager deckManager = DeckManager.singleton();

    @Override
    @BeforeEach
    void init() throws SQLException, OpenedDatabaseException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Test
    void getTag_TagNotExists_ReturnsNull() {
        Tag tag = new Tag("name");
        assertEquals(null, tagManager.getTag(tag.getId()));
    }

    @Test
    void saveTag_TagNotExists_TagExists() {
        Tag tag = new Tag("name");
        tagManager.saveTag(tag);

        assertEquals(tag, tagManager.getTag(tag.getId()));
    }

    @Test
    void getAllTags_NoTags_EmptyList() {
        assertTrue(tagManager.getAllTags().isEmpty());
    }

    @Test
    void getAllTags_ManyTags_AllReturned() {
        List<Tag> tags = new ArrayList();
        tags.add(new Tag("name1"));
        tags.add(new Tag("name2"));
        tags.add(new Tag("name3"));

        tags.forEach((d) -> tagManager.saveTag(d));

        assertEquals(new HashSet(tags), new HashSet(tagManager.getAllTags()));
    }

    @Test
    void getAllTags_SameTagAddedMultipleTimes_OneReturned() {
        Tag tag = new Tag("name");
        tagManager.saveTag(tag);
        tagManager.saveTag(tag);
        tagManager.saveTag(tag);

        assertEquals(Set.of(tag), new HashSet(tagManager.getAllTags()));
    }

    @Test
    void deleteTag_TagExists_TagNotExists() {
        Tag tag = new Tag("name");
        tagManager.saveTag(tag);
        tagManager.deleteTag(tag);

        assertEquals(null, tagManager.getTag(tag.getId()));
    }

    @Test
    void deleteTag_TagNotExists_NoThrow() {
        Tag tag = new Tag("name");
        assertDoesNotThrow(() -> tagManager.deleteTag(tag));
    }


//    @Test
//    void getTagsFor_DeckExists_ReturnListTags() throws DatabaseNotInitException, DeckNotExistsException, TagNotExistsException {
//        Deck deck = deckManager.createDeck("test");
//        Tag tag = new Tag("test");
//        tagManager.addTag(deck, tag);
//        assertEquals(tagManager.getTagsFor(deck.getId()).size(), 1);
//        assertEquals(tagManager.getTagsFor(deck.getId()).get(0).getId(), tag.getId());
//        assertEquals(tagManager.getTagsFor(deck.getId()).get(0).getName(), tag.getName());
//        assertEquals(tagManager.getTagsFor(deck.getId()).get(0).getColor(), tag.getColor());
//        tagManager.delTag(tag);
//        deckManager.delDeck(deck);
//    }

//    @Test
//    void getTagsFor_DeckNotExists_ThrowsException() {
//        assertThrows(DeckNotExistsException.class, () -> tagManager.getTagsFor(new Deck("test").getId()));
//    }

//    @Test
//    void getAllTags_ReturnListTags() throws DatabaseNotInitException, DeckNotExistsException, TagNotExistsException {
//        Deck deck = deckManager.createDeck("test");
//        Tag tag = new Tag("test");
//        tagManager.addTag(deck, tag);
//        assertEquals(tagManager.getAllTags().size(), 1);
//        assertEquals(tagManager.getAllTags().get(0).getId(), tag.getId());
//        assertEquals(tagManager.getAllTags().get(0).getName(), tag.getName());
//        assertEquals(tagManager.getAllTags().get(0).getColor(), tag.getColor());
//        tagManager.delTag(tag);
//        deckManager.delDeck(deck);
//    }
}
