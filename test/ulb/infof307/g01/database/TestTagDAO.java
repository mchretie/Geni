package ulb.infof307.g01.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.database.exceptions.DatabaseException;
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
    void getTag_TagNotExists_ReturnsNull() {
        Tag tag = new Tag("name");
        assertEquals(null, tagDAO.getTag(tag.getId()));
    }

    @Test
    void tagNameExists_NameNotExists_ReturnsFalse() {
        assertFalse(tagDAO.tagNameExists("name"));
    }

    @Test
    void tagNameExists_NameExists_ReturnsTrue() {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);

        assertTrue(tagDAO.tagNameExists("name"));
    }

    @Test
    void isTagValid_TagInvalid_ReturnsFalse() {
        Tag tag1 = new Tag("name");
        Tag tag2 = new Tag("name");
        tagDAO.saveTag(tag1);

        assertFalse(tagDAO.isTagValid(tag2));
    }

    @Test
    void isTagValid_TagValid_ReturnsTrue() {
        Tag tag = new Tag("name");
        assertTrue(tagDAO.isTagValid(tag));

        tagDAO.saveTag(tag);
        assertTrue(tagDAO.isTagValid(tag));
    }

    @Test
    void saveTag_TagsWithSameNameDiffId_OnlyFirstAdded() {
        Tag tag1 = new Tag("name");
        Tag tag2 = new Tag("name");

        tagDAO.saveTag(tag1);
        tagDAO.saveTag(tag2);

        assertEquals(Set.of(tag1), new HashSet(tagDAO.getAllTags()));
    }

    @Test
    void saveTag_TagNotExists_TagExists() {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);

        assertEquals(tag, tagDAO.getTag(tag.getId()));
    }

    @Test
    void getAllTags_NoTags_EmptyList() {
        assertTrue(tagDAO.getAllTags().isEmpty());
    }

    @Test
    void getAllTags_ManyTags_AllReturned() {
        List<Tag> tags = new ArrayList();
        tags.add(new Tag("name1"));
        tags.add(new Tag("name2"));
        tags.add(new Tag("name3"));

        tags.forEach((d) -> tagDAO.saveTag(d));

        assertEquals(new HashSet(tags), new HashSet(tagDAO.getAllTags()));
    }

    @Test
    void getAllTags_SameTagAddedMultipleTimes_OneReturned() {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);
        tagDAO.saveTag(tag);
        tagDAO.saveTag(tag);

        assertEquals(Set.of(tag), new HashSet(tagDAO.getAllTags()));
    }

    @Test
    void saveTagsFor_TagsAddedToDeck_SaveTags() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagDAO.saveTagsFor(deck);

        assertEquals(new HashSet(deck.getTags()),
                new HashSet(tagDAO.getTagsFor(deck.getId())));
    }

    @Test
    void saveTagsFor_TagsRemovedFromDeck_SaveTags() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagDAO.saveTagsFor(deck);
        deck.removeTag(tag);
        tagDAO.saveTagsFor(deck);

        assertEquals(new HashSet(deck.getTags()),
                new HashSet(tagDAO.getTagsFor(deck.getId())));
    }

    @Test
    void getTagsFor_NoTags_EmptyList() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);
        tagDAO.saveTagsFor(deck);
        assertTrue(tagDAO.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getTagsFor_DeckNotExist_EmptyList() {
        Deck deck = new Deck("name");
        assertTrue(tagDAO.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getDecksHavingTag_NoDecks_EmptyList() {
        Tag tag = new Tag("name");
        assertTrue(tagDAO.getDecksHavingTag(tag).isEmpty());
    }

    @Test
    void getDecksHavingTag_MultipleDecks_OnlyThoseReturned() {
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
        var receivedSet = new HashSet<Deck>(tagDAO.getDecksHavingTag(tag));

        assertEquals(expectedSet, receivedSet);
    }

    @Test
    void deleteTag_TagExists_TagNotExists() {
        Tag tag = new Tag("name");
        tagDAO.saveTag(tag);
        tagDAO.deleteTag(tag);

        assertEquals(null, tagDAO.getTag(tag.getId()));
    }

    @Test
    void deleteTag_TagNotExists_NoThrow() {
        Tag tag = new Tag("name");
        assertDoesNotThrow(() -> tagDAO.deleteTag(tag));
    }
}
