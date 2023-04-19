package ulb.infof307.g01.gui.util;

import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.deck.Deck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Import and export decks to files
 * <p>
 *     
 * </p>
 */
public class DeckIO {

    public final String EXT = "flashcards";

    public void export(Deck deck, Path exportPath) throws IOException, IllegalArgumentException {
        if (deck == null
                || exportPath == null
                || exportPath.getFileName().toString().equals(""))
            throw new IllegalArgumentException("Wrong arguments given");

        if (!hasExtension(exportPath))
            exportPath = addExtension(exportPath);

        Files.writeString(exportPath, deck.toJson());
    }

    public Deck importFrom(Path path) throws IOException, IllegalArgumentException {
        if (path == null || Files.notExists(path))
            throw new IllegalArgumentException("Path doesn’t exist or null");

        String json = Files.readString(path);
        Deck deck = Deck.fromJson(json);
        
        deck.generateNewId();
        for (Card card : deck.getCards())
            card.generateNewId();

        return deck;
    }

    private boolean hasExtension(Path path) {
        return path.getFileName().toString().endsWith("." + EXT);
    }

    private Path addExtension(Path file) {
        return Path.of(file.toString() + "." + EXT);
    }
}
