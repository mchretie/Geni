package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Objects;
import java.util.Iterator;


public class Deck implements Iterable<Card> {
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

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags) {
        this.name = name;
        this.id = id;
        this.cards = cards;
        this.tags = tags;
    }

    public List<Tag> getTags() { return tags; }

    public List<Card> getCards() { return cards; }
    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int cardCount() { return cards.size(); }

    public void addTag(Tag tag) { tags.add(tag); }

    public void addCard(Card card) {
        card.setDeckId(id);
        cards.add(card);
    }

    public void addCards(List<Card> cards) {
        cards.forEach((c) -> addCard(c));
    }

    public void removeTag(Tag tag) { tags.remove(tag); }
    public void removeCard(Card card) { cards.remove(card); }

    public Iterable<Card> inOrder() { return cards; }

    public Iterable<Card> randomOrder() {
        List<Card> copy = new ArrayList<>( cards );
        Collections.shuffle(copy);
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Deck other = (Deck) obj;
        return this.id.equals(other.id)
            && this.name.equals(other.name)
            && this.cards.equals(other.cards)
            && this.tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags);
    }

    public Iterator<Card> iterator() {
        return cards.iterator();

    }
}
