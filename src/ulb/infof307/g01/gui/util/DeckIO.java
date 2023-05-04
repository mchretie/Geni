package ulb.infof307.g01.gui.util;

import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Import and export decks to files
 * <p>
 * The decks imported from here should be considered
 * unique in their ids and their name different from
 * the decks set in {@link DeckIO#setAllDecks(List)}.
 * </p>
 */
public class DeckIO {

    public final String EXT = "deck";

    private List<DeckMetadata> allDecksMetadata = Collections.emptyList();

    /**
     * Export to deck to given path
     * <p>
     * The path extension is determined by {@link DeckIO#EXT}.
     * If not in the path provided, it is appended to it.
     * </p>
     *
     * @param deck       Deck to be exported
     * @param exportPath File to which the deck is exported
     * @throws IOException              Error with writing to the path
     * @throws IllegalArgumentException Incorrect path or deck given (or null)
     */
    public void export(Deck deck, Path exportPath) throws IOException, IllegalArgumentException {
        if (deck == null
                || exportPath == null
                || exportPath.getFileName().toString().equals(""))
            throw new IllegalArgumentException("Wrong arguments given");

        if (!hasExtension(exportPath))
            exportPath = addExtension(exportPath);

        Files.writeString(exportPath, deck.toJson());
    }

    /**
     * Import deck from given path
     * <p>
     * The imported deck may have it’s original name changed in order
     * to not conflict with the other decks names.
     * </p>
     *
     * @param path Path from which the deck is imported
     * @return The imported deck with brand-new ids and potentially a new name.
     * @throws IOException              Issue with reading from the path
     * @throws IllegalArgumentException Incorrect or null path given
     * @see ulb.infof307.g01.gui.util.DeckIO#setAllDecks(List);
     */
    public Deck importFrom(Path path) throws IOException, IllegalArgumentException {
        if (path == null || Files.notExists(path))
            throw new IllegalArgumentException("Path doesn’t exist or null");

        String json = Files.readString(path);
        Deck deck = Deck.fromJson(json);

        deck.generateNewId();

        deck.setName(getUniqueNameFrom(deck.getName()));

        return deck;
    }

    /**
     * Set the decks against which the imported deck’s name will be compared to
     *
     * @param decksMetadata The decks to check for conflicts
     */
    public void setAllDecks(List<DeckMetadata> decksMetadata) {
        this.allDecksMetadata = decksMetadata;
    }

    /**
     * Create a unique name from a base one if not unique
     * <p>
     * The name will be the same as the original name
     * with a number in parentheses.
     * </p>
     *
     * @param baseName the deck to assign a name to
     */
    private String getUniqueNameFrom(String baseName) {
        if (!matchAny(baseName))
            return baseName;

        var idx = 1;
        String newName = baseName;
        do {
            newName = "%s (%d)".formatted(baseName, idx);
            idx++;
        } while (matchAny(newName));

        return newName;
    }

    private boolean matchAny(String deckName) {
        return allDecksMetadata.stream()
                .anyMatch(d -> deckName.equals(d.name()));
    }

    private boolean hasExtension(Path path) {
        return path.getFileName().toString().endsWith("." + EXT);
    }

    private Path addExtension(Path file) {
        return Path.of(file.toString() + "." + EXT);
    }
}
