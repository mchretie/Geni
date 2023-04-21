package ulb.infof307.g01.gui.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckIO {

    DeckIO deckIO = new DeckIO();

    String f1 = "test_1";
    String f2 = f1 + "." + deckIO.EXT;
    String f3 = "test_2." + deckIO.EXT;

    Path tmpDir = null;
    Path path1 = Path.of("test_1");
    Path path2 = Path.of(path1 + "." + deckIO.EXT);
    Path path3 = Path.of("test_2" + "." + deckIO.EXT);

    @BeforeEach
    void setUp() throws IOException {
        tmpDir = Files.createTempDirectory(null);
        path1 = tmpDir.resolve(f1);
        path2 = tmpDir.resolve(f2);
        path3 = tmpDir.resolve(f3);
    }

    @AfterEach
    void tearDown() throws IOException {
        assert Files.walk(tmpDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .allMatch(File::delete);
    }

    @Test
    void export_WrongArgs_Throws() throws IOException {
        assertThrows(IllegalArgumentException.class,
                     () -> deckIO.export(null, null));
        assertThrows(IllegalArgumentException.class,
                     () -> deckIO.export(new Deck("n"), null));
        assertThrows(IllegalArgumentException.class,
                     () -> deckIO.export(new Deck("n"), Path.of("")));
    }

    @Test
    void export_FileWithoutExt_ExtAdded() throws IOException {
        Deck deck = new Deck("name");
        deckIO.export(deck, path1);

        assertTrue(Files.exists(path2));
    }

    @Test
    void export_DeckExported_FileExists() throws IOException {
        Deck deck = new Deck("name");
        deckIO.export(deck, path2);

        assertTrue(Files.exists(path2));
    }

    @Test
    void export_TwiceExported_SameContent() throws IOException {
        Deck deck = new Deck("name");
        deckIO.export(deck, path2);
        deckIO.export(deck, path3);

        assertEquals(Files.readString(path2), Files.readString(path3));
    }

    @Test
    void export_WithCards_Exported() throws IOException {
        Deck deck = new Deck("name");

        deck.addCard(new FlashCard());
        deck.addCard(new MCQCard());
        deck.addCard(new InputCard());

        deckIO.export(deck, path2);

        assertTrue(Files.exists(path2));
    }

    @Test
    void importFrom_NoCards_DifferentId() throws IOException {
        Deck deck1 = new Deck("name");
        deckIO.export(deck1, path2);
        Deck deck2 = deckIO.importFrom(path2);

        assertNotEquals(deck1.getId(), deck2.getId());
    }

    @Test
    void importFrom_NoDecksSet_SameName() throws IOException {
        Deck deck1 = new Deck("name");
        deckIO.export(deck1, path2);
        Deck deck2 = deckIO.importFrom(path2);

        assertEquals(deck1.getName(), deck2.getName());
    }

    @Test
    void importFrom_DecksSet_DifferentName() throws IOException {
        Deck deck1 = new Deck("name");
        deckIO.setAllDecks(List.of(deck1.getMetadata()));
        deckIO.export(deck1, path2);
        Deck deck2 = deckIO.importFrom(path2);

        assertNotEquals(deck1.getName(), deck2.getName());
    }

    // TODO: better test the equality of cards
    @Test
    void importFrom_WithCards_SameCards() throws IOException {
        Deck deck1 = new Deck("name");

        deck1.addCard(new FlashCard());
        deck1.addCard(new MCQCard());
        deck1.addCard(new InputCard());

        deckIO.export(deck1, path2);
        Deck deck2 = deckIO.importFrom(path2);

        assertEquals(deck1.cardCount(), deck2.cardCount());
    }

    @Test
    void importFrom_WithTags_SameTags() throws IOException {
        Deck deck1 = new Deck("name");

        deck1.addTag(new Tag("name"));

        deckIO.export(deck1, path2);
        Deck deck2 = deckIO.importFrom(path2);

        assertEquals(deck1.getTags(), deck2.getTags());
    }
}