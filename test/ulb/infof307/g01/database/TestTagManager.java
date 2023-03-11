package ulb.infof307.g01.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    void saveTag_TagsWithSameNameDiffId_OnlyFirstAdded() {
        Tag tag1 = new Tag("name");
        Tag tag2 = new Tag("name");

        tagManager.saveTag(tag1);
        tagManager.saveTag(tag2);

        assertEquals(Set.of(tag1), new HashSet(tagManager.getAllTags()));
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
    void saveTagsFor_TagsAddedToDeck_SaveTags() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagManager.saveTagsFor(deck);

        assertEquals(new HashSet(deck.getTags()),
                new HashSet(tagManager.getTagsFor(deck.getId())));
    }

    @Test
    void saveTagsFor_TagsRemovedFromDeck_SaveTags() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");
        deck.addTag(tag);
        tagManager.saveTagsFor(deck);
        deck.removeTag(tag);
        tagManager.saveTagsFor(deck);

        assertEquals(new HashSet(deck.getTags()),
                new HashSet(tagManager.getTagsFor(deck.getId())));
    }

    @Test
    void getTagsFor_NoTags_EmptyList() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);
        tagManager.saveTagsFor(deck);
        assertTrue(tagManager.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getTagsFor_DeckNotExist_EmptyList() {
        Deck deck = new Deck("name");
        assertTrue(tagManager.getTagsFor(deck.getId()).isEmpty());
    }

    @Test
    void getDecksHavingTag_NoDecks_EmptyList() {
        Tag tag = new Tag("name");
        assertTrue(tagManager.getDecksHavingTag(tag).isEmpty());
    }

    @Test
    void getDecksHavingTag_MultipleDecks_OnlyThoseReturned() {
        Deck deck1 = new Deck("name1");
        Deck deck2 = new Deck("name2");
        Deck deck3 = new Deck("name3");

        deckManager.saveDeck(deck1);
        deckManager.saveDeck(deck2);
        deckManager.saveDeck(deck3);

        Tag tag = new Tag("name");
        deck1.addTag(tag);
        deck2.addTag(tag);

        tagManager.saveTagsFor(deck1);
        tagManager.saveTagsFor(deck2);
        tagManager.saveTagsFor(deck3);

        var expectedSet = Set.of(deck1, deck2);
        var receivedSet = new HashSet<Deck>(tagManager.getDecksHavingTag(tag));

        assertEquals(expectedSet, receivedSet);
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
}
