package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class Deck {
    private List<Card> cards = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public void addTag(Tag tag) { tags.add(tag); }
    public void removeTag(Tag tag) { tags.remove(tag); }

    public void addCard(Card card) { cards.add(card); }
    public void removeCard(Card card) { cards.remove(card); }

    public int size() { return cards.size(); }

}
