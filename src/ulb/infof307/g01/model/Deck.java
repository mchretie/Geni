package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Deck {
    private String name;
    private final UUID id = UUID.randomUUID();
    private List<Card> cards = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public Deck(String name) {
        this.name = name;
    }

    public List<Tag> getTags() { return tags; }
    public UUID getId() { return id; }
    public String getName() { return name; }

    public int cardCount() { return cards.size(); }

    public void addTag(Tag tag) { tags.add(tag); }
    public void addCard(Card card) { cards.add(card); }

    public void removeTag(Tag tag) { tags.remove(tag); }
    public void removeCard(Card card) { cards.remove(card); }

    public Iterable<Card> inOrder() { return cards; }

    public Iterable<Card> randomOrder() {
        List<Card> copy = new ArrayList<>( cards );
        Collections.shuffle(copy);
        return copy;
    }
}
