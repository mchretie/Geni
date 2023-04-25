package ulb.infof307.g01.model.deck;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.InputCard;
import ulb.infof307.g01.model.card.MCQCard;

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
    private String color;
    @Expose
    private String image;

    public Deck(String name) {
        this(name,
             UUID.randomUUID());
    }

    public Deck(String name, UUID id) {
        this(name,
             id,
             new ArrayList<>(),
             new ArrayList<>());
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags) {
        this(name,
             id,
             cards,
             tags,
             "#00000000",
             "/backgrounds/default_background.jpg");
    }

    public Deck(String name, UUID id, List<Card> cards, List<Tag> tags, String color, String image) {
        this.name = name;
        this.id = id;
        this.cards = cards;
        this.tags = tags;
        this.color = color;
        this.image = image;
    }

    public Deck(Deck deck) {
        this(deck.name, deck.id, deck.cards, deck.tags, deck.color, deck.image);
    }

    public static Deck fromJson(String json) {
        Deck deck = new Gson().fromJson(json, Deck.class);

        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonArray cardsJson = jsonObject.getAsJsonArray("cards");
        deck.setCardsFromJson(cardsJson);

        return deck;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }


    /**
     * Generate a new id for the deck and its cards
     * <p>
     * A new id is also generated for the cards because not doing so
     * can only lead to a broken database.
     * </p>
     */
    public void generateNewId() {
        this.id = UUID.randomUUID();
        for (Card card : cards) {
            card.setDeckId(this.id);
            card.generateNewId();
        }
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Card getCard(int index) throws IndexOutOfBoundsException {
        return cards.get(index);
    }

    public Card getLastCard() throws IndexOutOfBoundsException {
        return getCard(cards.size() - 1);

    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
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

    public boolean tagExists(final String newTagName) {
        return tags.stream()
                .anyMatch(tag -> newTagName.equals(tag.getName()));
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
                                name,
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

    public void setCardsFromJson(JsonArray cardsJson) {
        this.cards.clear();
        for (JsonElement card : cardsJson) {
            JsonObject cardObject = card.getAsJsonObject();
            String cardType = cardObject.get("cardType").getAsString();
            switch (cardType) {
                case "FlashCard" ->
                        this.cards.add(new Gson().fromJson(card, FlashCard.class));
                case "MCQCard" ->
                        this.cards.add(new Gson().fromJson(card, MCQCard.class));
                case "InputCard" ->
                        this.cards.add(new Gson().fromJson(card, InputCard.class));
            }
        }
    }
}
