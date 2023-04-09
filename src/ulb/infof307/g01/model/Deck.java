package ulb.infof307.g01.model;

import com.google.gson.annotations.Expose;

import java.util.*;


public class Deck implements Iterable<Card> {

  private UUID id;

  @Expose
  private String name;
  @Expose
  private final List<Card> cards;
  @Expose
  private final List<Tag> tags;
  @Expose
  private String color = "0x00000000";

  @Expose
  private String image;

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

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags, String color, String image) {
        this.name = name;
        this.id = id;
        this.cards = cards;
        this.tags = tags;
        this.color = color;
        this.image = image;
    }

    public void setNewID() {
        this.id = UUID.randomUUID();

        getCards().forEach(card -> card.setDeckId(this.id));
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Card getCard(int index) throws IndexOutOfBoundsException {
        return cards.get(index);
    }

    public Card getLastCard() throws IndexOutOfBoundsException {
        return getCard(cards.size() - 1);

    }

    public Card getFirstCard() throws IndexOutOfBoundsException {
        return getCard(0);
    }

    public List<Card> getCards() {
        return cards;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int cardCount() {
        return cards.size();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addCard(Card card) {
        card.setDeckId(id);
        cards.add(card);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public boolean tagExists(String newTagName) {

        for (Tag tag : tags) {
            if (tag.getName().equals(newTagName))
                return true;
        }

        return false;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DeckMetadata getMetadata() {
        return new DeckMetadata(id,
                                color,
                                image,
                                cards.size(),
                                tags,
                                hashCode());
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
                && this.tags.equals(other.tags)
                && this.color.equals(other.color)
                && this.image.equals(other.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags, color, image);
    }

    public Iterator<Card> iterator() {
        return cards.iterator();

    }
}
