package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Deck {
    private String name;
    private UUID id;
    private List<Card> cards;
    private List<Tag> tags;

    public Deck(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.cards = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Deck(String name, UUID id) {
        this.name = name;
        this.id = id;
        this.cards = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public List<Tag> getTags() { return tags; }
    public UUID getId() { return id; }
    public String getName() { return name; }

    public int cardCount() { return cards.size(); }

    public void addTag(Tag tag) { tags.add(tag); }
    public void addCard(Card card) { cards.add(card); }

    public void addCards(List<Card> cards) { this.cards.addAll(cards); }

    public void removeTag(Tag tag) { tags.remove(tag); }
    public void removeCard(Card card) { cards.remove(card); }

    public Iterable<Card> inOrder() { return cards; }

    public Iterable<Card> randomOrder() {
        List<Card> copy = new ArrayList<>( cards );
        Collections.shuffle(copy);
        return copy;
    }
}
