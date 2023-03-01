package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;


public class Deck {
    private String name;
    private List<Card> cards = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public Deck(String name) {
        this.name = name;
    }

    public void addTag(Tag tag) { tags.add(tag); }
    public void removeTag(Tag tag) { tags.remove(tag); }
    public List<Tag> getTags() { return tags; }

    public void addCard(Card card) { cards.add(card); }
    public void removeCard(Card card) { cards.remove(card); }

    public int cardCount() { return cards.size(); }

    public Iterable<Card> inOrder() { return cards; }

    public Iterable<Card> randomOrder() {
        List<Card> copy = new ArrayList<>( cards );
        Collections.shuffle(copy);
        return copy;
    }
}
