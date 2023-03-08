package ulb.infof307.g01.database;

import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTagManager {
    TagManager tm = TagManager.singleton();
    DeckManager dm = DeckManager.singleton();

    @Test
    void getTag_TagExists_ReturnTag() {
        dm.createDeck("test");
        Deck deck = dm.getDeck("test");
        Tag tag = new Tag("test");
        tm.addTag(deck, tag);
        assertEquals(tm.getTag(tag.getId()), tag);
        tm.delTag(tag);
    }

    @Test
    void getTag_TagNotExists_ThrowsException() {
        assertThrows(TagNotExistsException.class, () -> tm.getTag(new Tag("test").getId()));
    }


    @Test
    void getTagsFor_DeckExists_ReturnListTags() {
        dm.createDeck("test");
        Deck deck = dm.getDeck("test");
        Tag tag = new Tag("test");
        tm.addTag(deck, tag);
        assertEquals(tm.getTagsFor(deck.getId()).size(), 1);
        assertEquals(tm.getTagsFor(deck.getId()).get(0), tag);
        tm.delTag(tag);
    }

    @Test
    void getTagsFor_DeckNotExists_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> tm.getTagsFor(new Deck("test").getId()));
    }

    @Test
    void getAllTags_ReturnListTags() {
        dm.createDeck("test");
        Deck deck = dm.getDeck("test");
        Tag tag = new Tag("test");
        tm.addTag(deck, tag);
        assertEquals(tm.getAllTags().size(), 1);
        assertEquals(tm.getAllTags().get(0), tag);
        tm.delTag(tag);
    }

//    @Test
//    void addTag_TagUnique_TagInDB() {
//
//    }
//
//    @Test
//    void addTag_TagNotUnique_ThrowsException() {
//
//    }


    @Test
    void delTag_TagExists_TagNotInDB() {
        dm.createDeck("test");
        Deck deck = dm.getDeck("test");
        Tag tag = new Tag("test");
        tm.addTag(deck, tag);
        tm.delTag(tag);
        assertThrows(TagNotExistsException.class, () -> tm.getTag(tag.getId()));
    }

    @Test
    void delTag_TagNotExists_ThrowsException() {
        assertThrows(TagNotExistsException.class, () -> tm.delTag(new Tag("test")));
    }

}
