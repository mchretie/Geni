package ulb.infof307.g01.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.gui.database.DatabaseScheme;
import ulb.infof307.g01.gui.database.DeckDAO;
import ulb.infof307.g01.gui.database.TagDAO;
import ulb.infof307.g01.gui.database.exceptions.DatabaseException;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagDAO extends DatabaseUsingTest {

    TagDAO tagDAO = TagDAO.singleton();
    DeckDAO deckDAO = DeckDAO.singleton();

    @Override
    @BeforeEach
    void init() throws SQLException, DatabaseException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Test
    void getTag_TagNotExists_ReturnsNull() throws SQLException {
        Tag tag = new Tag("name");
        assertNull(tagDAO.getTag(tag.getId()));
    }

    @Test
    void tagNameExists_NameNotExists_ReturnsFalse() throws SQLException {
        assertFalse(tagDAO.tagNameExists("name"));
    }

    @Test
    void tagNameExists_NameExists_ReturnsTrue() throws SQLException {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);

        assertTrue(tagDAO.tagNameExists("name"));
    }

    @Test
    void isTagValid_TagInvalid_ReturnsFalse() throws SQLException {
        Tag tag1 = new Tag("name");
        Tag tag2 = new Tag("name");
        tagDAO.saveTag(tag1);

        assertFalse(tagDAO.isTagValid(tag2));
    }

    @Test
    void isTagValid_TagValid_ReturnsTrue() throws SQLException {
        Tag tag = new Tag("name");
        assertTrue(tagDAO.isTagValid(tag));

        tagDAO.saveTag(tag);
        assertTrue(tagDAO.isTagValid(tag));
    }

    @Test
    void saveTag_TagsWithSameNameDiffId_OnlyFirstAdded() throws SQLException {
        Tag tag1 = new Tag("name");
        Tag tag2 = new Tag("name");

        tagDAO.saveTag(tag1);
        tagDAO.saveTag(tag2);

        assertEquals(Set.of(tag1), new HashSet<>(tagDAO.getAllTags()));
    }

    @Test
    void saveTag_TagNotExists_TagExists() throws SQLException {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);

        assertEquals(tag, tagDAO.getTag(tag.getId()));
    }

    @Test
    void getAllTags_NoTags_EmptyList() throws SQLException {
        assertTrue(tagDAO.getAllTags().isEmpty());
    }

    @Test
    void getAllTags_ManyTags_AllReturned() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("name1"));
        tags.add(new Tag("name2"));
        tags.add(new Tag("name3"));

        for (Tag d : tags)
            tagDAO.saveTag(d);


        assertEquals(new HashSet<>(tags), new HashSet<>(tagDAO.getAllTags()));
    }

    @Test
    void getAllTags_SameTagAddedMultipleTimes_OneReturned() throws SQLException {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);
        tagDAO.saveTag(tag);
        tagDAO.saveTag(tag);

        assertEquals(Set.of(tag), new HashSet<>(tagDAO.getAllTags()));
    }

    @Test
    void saveTagsFor_TagsAddedToDeck_SaveTags() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagDAO.saveTagsFor(deck);

        assertEquals(new HashSet<>(deck.getTags()),
                new HashSet<>(tagDAO.getTagsFor(deck.getId())));
    }

    @Test
    void saveTagsFor_TagsRemovedFromDeck_SaveTags() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagDAO.saveTagsFor(deck);
        deck.removeTag(tag);
        tagDAO.saveTagsFor(deck);

        assertEquals(new HashSet<>(deck.getTags()),
                new HashSet<>(tagDAO.getTagsFor(deck.getId())));
    }

    @Test
    void getTagsFor_NoTags_EmptyList() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);
        tagDAO.saveTagsFor(deck);
        assertTrue(tagDAO.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getTagsFor_DeckNotExist_EmptyList() throws SQLException {
        Deck deck = new Deck("name");
        assertTrue(tagDAO.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getDecksHavingTag_NoDecks_EmptyList() throws SQLException {
        Tag tag = new Tag("name");
        assertTrue(tagDAO.getDecksHavingTag(tag).isEmpty());
    }

    @Test
    void getDecksHavingTag_MultipleDecks_OnlyThoseReturned() throws SQLException {
        Deck deck1 = new Deck("name1");
        Deck deck2 = new Deck("name2");
        Deck deck3 = new Deck("name3");

        deckDAO.saveDeck(deck1);
        deckDAO.saveDeck(deck2);
        deckDAO.saveDeck(deck3);

        Tag tag = new Tag("name");
        deck1.addTag(tag);
        deck2.addTag(tag);

        tagDAO.saveTagsFor(deck1);
        tagDAO.saveTagsFor(deck2);
        tagDAO.saveTagsFor(deck3);

        var expectedSet = Set.of(deck1, deck2);
        var receivedSet = new HashSet<>(tagDAO.getDecksHavingTag(tag));

        assertEquals(expectedSet, receivedSet);
    }

    @Test
    void deleteTag_TagExists_TagNotExists() throws SQLException {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);
        tagDAO.deleteTag(tag);

        assertNull(tagDAO.getTag(tag.getId()));
    }

    @Test
    void deleteTag_TagNotExists_NoThrow() {
        Tag tag = new Tag("name");
        assertDoesNotThrow(() -> tagDAO.deleteTag(tag));
    }

    @Test
    void searchTags() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("name1"));
        tags.add(new Tag("name2"));
        tags.add(new Tag("name3"));

        for (Tag d : tags)
            tagDAO.saveTag(d);

        assertEquals(new HashSet<>(tags), new HashSet<>(tagDAO.searchTags("name")));
    }
}
